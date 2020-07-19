package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class NewCharacterViewModel(

) : ViewModel() {

    lateinit var characterPagedList: LiveData<PagedList<Character>>
    lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Character>>

    @ExperimentalCoroutinesApi
    lateinit var onProgress: CharacterDataSource.OnProgress

    @ExperimentalCoroutinesApi
    fun initialize(
        lifecycleOwner: LifecycleOwner,
        characterViewModel: CharacterViewModel,
        onProgress: CharacterDataSource.OnProgress
    ) {
        val characterDataSourceFactory = CharacterDataSourceFactory(
            characterViewModel = characterViewModel,
            lifecycleOwner = lifecycleOwner,
            onProgress = onProgress

        )
        liveDataSource = characterDataSourceFactory.getCharacterLiveDataSource()
        val config = PagedList.Config.Builder().setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()
    }


}