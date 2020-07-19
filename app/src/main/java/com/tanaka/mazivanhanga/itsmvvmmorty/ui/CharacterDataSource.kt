package com.tanaka.mazivanhanga.itsmvvmmorty.ui;

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.tanaka.mazivanhanga.itsmvvmmorty.di.RetrofitModule
import com.tanaka.mazivanhanga.itsmvvmmorty.di.RoomModule
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.networking.NetworkMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CacheMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CharacterDao
import com.tanaka.mazivanhanga.itsmvvmmorty.util.DataState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CharacterDataSource(
    private val viewModel: CharacterViewModel,
    private val lifecycleOwner: LifecycleOwner
) : PageKeyedDataSource<Int, Character>() {

    interface OnProgress {
        fun displayProgressBar(shouldDisplay: Boolean)
        fun displayError(message: String?)
        fun getContext(): Context
        fun clearDisposable(compositeDisposable: CompositeDisposable)
    }

    private lateinit var onProgress: OnProgress
    fun setOnprogress(onProgress: OnProgress) {
        this.onProgress = onProgress
    }


    private val MAX_PAGES = 30
    val INITIAL_PAGE = 1
    var characters: ArrayList<Character> = ArrayList()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Character>
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val db = RoomModule.provideCharacterDb(context = onProgress.getContext())
            val dao = RoomModule.provideCharacterDao(db)
            subscribeInitialObserver(dao, callback)
        }
        viewModel.page = INITIAL_PAGE
        viewModel.setStateEvent(CharacterStateEvent.GetCharacterEvent)
    }

    private fun subscribeInitialObserver(
        dao: CharacterDao,
        callback: LoadInitialCallback<Int, Character>
    ) {
        viewModel.dataState.observe(
            lifecycleOwner, Observer {
                when (it) {
                    is DataState.Success<List<Character>> -> {
                        onProgress.displayProgressBar(false)
                        CoroutineScope(Dispatchers.IO).launch {
                            val cachedCharacters = dao.getCharacters()
                            val cacheMapper = CacheMapper()
                            characters =
                                cacheMapper.mapFromEntitiesList(cachedCharacters) as ArrayList<Character>


                        }
                        callback.onResult(it.data, null, INITIAL_PAGE + 1)
                    }
                    is DataState.Error -> {
                        onProgress.displayProgressBar(false)
                        onProgress.displayError(it.exception.message)
                    }
                    is DataState.Loading -> {
                        onProgress.displayProgressBar(true)
                    }
                }
            }
        )
    }

    private val gson = RetrofitModule.provideGsonBuilder()
    private val retrofit = RetrofitModule.provideRetrofit(gson)
    private val service = RetrofitModule.provideCharacterService(retrofit)
    var maxRecords = 591
    var compositeDisposable = CompositeDisposable()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        val key = if (params.key < MAX_PAGES) params.key + 1 else null
        viewModel.page = params.key
        println(">>> Size of characters ${characters.size}")
        if (characters.size < maxRecords) {
            onProgress.displayProgressBar(true)
            val db = RoomModule.provideCharacterDb(context = onProgress.getContext())
            val dao = RoomModule.provideCharacterDao(db)
            if (params.key != 1) {
                compositeDisposable.add(
                    getLoadAfterDisposable(params, dao, callback, key)
                )
            }
        } else {
            onProgress.clearDisposable(compositeDisposable)
        }
    }

    private fun getLoadAfterDisposable(
        params: LoadParams<Int>,
        dao: CharacterDao,
        callback: LoadCallback<Int, Character>,
        key: Int?
    ): Disposable {
        return service.getCharacterObservable(params.key).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                onProgress.displayProgressBar(false)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        maxRecords = response.body()?.info?.count ?: 591
                        val networkMapper = NetworkMapper()
                        val characterCacheMapper = CacheMapper()
                        val characters =
                            networkMapper.mapFromEntity(response.body()!!)
                        characters.forEach {
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insert(characterCacheMapper.mapToEntity(it))
                            }
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            val cachedCharacters = dao.getCharacters()
                            val cacheMapper = CacheMapper()
                            this@CharacterDataSource.characters =
                                cacheMapper.mapFromEntitiesList(cachedCharacters) as ArrayList<Character>
                        }
                        callback.onResult(characters, key)
                    }
                } else {
                    onProgress.displayError(response.errorBody().toString())
                }
            }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        val key = if (params.key > 1) params.key - 1 else null
        viewModel.page = params.key
        val db = RoomModule.provideCharacterDb(context = onProgress.getContext())
        val dao = RoomModule.provideCharacterDao(db)
        if (characters.size < maxRecords) {
            if (params.key != 1) {
                compositeDisposable.add(
                    getLoadBeforeDisposable(params, dao, callback, key)
                )
            } else {
                onProgress.clearDisposable(compositeDisposable)
            }
        }
        viewModel.setStateEvent(CharacterStateEvent.GetCharacterEvent)
        callback.onResult(characters, key)
    }

    private fun getLoadBeforeDisposable(
        params: LoadParams<Int>,
        dao: CharacterDao,
        callback: LoadCallback<Int, Character>,
        key: Int?
    ): Disposable {
        return service.getCharacterObservable(params.key).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                onProgress.displayProgressBar(false)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val networkMapper = NetworkMapper()
                        val characterCacheMapper = CacheMapper()
                        val characters =
                            networkMapper.mapFromEntity(response.body()!!)
                        characters.forEach {
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insert(characterCacheMapper.mapToEntity(it))
                            }
                        }
                        callback.onResult(characters, key)
                    }
                } else {
                    onProgress.displayError(response.errorBody().toString())
                }
            }
    }

}