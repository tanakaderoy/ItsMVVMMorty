package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CharacterDataSourceFactory @ExperimentalCoroutinesApi constructor(var lifecycleOwner: LifecycleOwner, var characterViewModel: CharacterViewModel, var onProgress: CharacterDataSource.OnProgress) : DataSource.Factory<Int, Character>() {
    private val characterLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Character>> =
        MutableLiveData()

    @ExperimentalCoroutinesApi
    override fun create(): DataSource<Int, Character> {
        val characterDataSource = CharacterDataSource(characterViewModel,lifecycleOwner)
        characterDataSource.setOnprogress(onProgress)
        characterLiveDataSource.postValue(characterDataSource)
        return characterDataSource
    }

    fun getCharacterLiveDataSource(): MutableLiveData<PageKeyedDataSource<Int, Character>>{
        return characterLiveDataSource
    }

}