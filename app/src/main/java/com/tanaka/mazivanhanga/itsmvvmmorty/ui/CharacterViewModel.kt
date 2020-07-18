package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.repository.MainRepository
import com.tanaka.mazivanhanga.itsmvvmmorty.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CharacterViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Character>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Character>>>
        get() = _dataState
    var page: Int = 0

    @ExperimentalCoroutinesApi
    fun setStateEvent(characterStateEvent: CharacterStateEvent) {
        viewModelScope.launch {
            when (characterStateEvent) {
                CharacterStateEvent.GetCharacterEvent -> {
                    mainRepository.getCharacter(page).onEach {
                        _dataState.value = it
                    }.launchIn(viewModelScope)
                }
                CharacterStateEvent.None -> {
//Nah Fam
                }
            }
        }
    }

}


sealed class CharacterStateEvent {
    object GetCharacterEvent : CharacterStateEvent()
    object None : CharacterStateEvent()
}
