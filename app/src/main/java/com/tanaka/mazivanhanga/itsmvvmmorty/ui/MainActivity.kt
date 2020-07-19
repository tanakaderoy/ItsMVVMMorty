package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanaka.mazivanhanga.itsmvvmmorty.R
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private val viewModel: CharacterViewModel by viewModels()
    private val onItemTouchListener = object : OnItemTouchListener {
        override fun onClicked(character: Character) {
            println("Clicked on ${character.name}")
            val bottomSheet = CharacterDetailBottomSheetDialog
            bottomSheet.character = character
            bottomSheet.show(supportFragmentManager, TAG)
        }

    }
    private val characterAdapter = CharacterAdapter(this, onItemTouchListener)
    lateinit var newCharacterViewModel: NewCharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        newCharacterViewModel = ViewModelProviders.of(this).get(NewCharacterViewModel::class.java)

        newCharacterViewModel.initialize(this, viewModel, onProgress)
        newCharacterViewModel.characterPagedList.observe(this, Observer {
            characterAdapter.submitList(it)
        })

        recyclerView.adapter = characterAdapter

    }

   private val onProgress = object : CharacterDataSource.OnProgress {
        override fun displayProgressBar(shouldDisplay: Boolean) {
            CoroutineScope(Main).launch {
                this@MainActivity.displayProgressBar(shouldDisplay)
            }

        }

        override fun displayError(message: String?) {
            CoroutineScope(Main).launch {
                this@MainActivity.displayError(message)

            }
        }

        override fun getContext(): Context {
            return this@MainActivity
        }

       override fun clearDisposable(compositeDisposable: CompositeDisposable) {
          compositeDisposable.clear()
       }

   }


    private fun subscribeObservers() {
        viewModel.characterDataState.observe(this, Observer {
            when (it) {
                is DataState.Success<Character> -> {
                    displayProgressBar(false)
                    println("Got Character detail ${it.data.name}")
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
        var error = ""
        if (message != null) {
            error = message
        } else {
            error = "Unkown Error"
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progressBar.visibility = if (isDisplayed) VISIBLE else GONE
    }
}
