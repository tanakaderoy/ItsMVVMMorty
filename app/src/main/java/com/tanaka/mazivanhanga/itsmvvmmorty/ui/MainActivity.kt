package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tanaka.mazivanhanga.itsmvvmmorty.R
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private val viewModel: CharacterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeObservers()
        viewModel.page = 1
        viewModel.setStateEvent(CharacterStateEvent.GetCharacterEvent)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer {
            when (it) {
                is DataState.Success<List<Character>> -> {
                    displayProgressBar(false)
                    handleSuccessCharacters(it.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if (message != null) {
            textView.text = message
        } else {
            textView.text = "Unkown Error"
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progressBar.visibility = if (isDisplayed) VISIBLE else GONE
    }

    private fun handleSuccessCharacters(characters: List<Character>) {
        val sb = StringBuilder()
        characters.forEach {
            sb.append("${it.name} \n")
        }

        textView.text = sb.toString()
    }
}
