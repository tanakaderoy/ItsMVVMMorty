package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var characters = ArrayList<Character>()
    var page = 1
    private val characterAdapter = CharacterAdapter(this, getOnItemTouchListener())

    companion object {
        private const val MAX_PAGES = 31
        private const val MAX_CHARACTERS = 591
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        initViews()
        subscribeObservers()

        viewModel.page = page
        initialGetCharacters()


    }

    private fun initialGetCharacters() {
        if (characters.isEmpty()) {
            viewModel.setStateEvent(CharacterStateEvent.GetCharacterEvent)
        }
    }

    private fun getOnScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (page < MAX_PAGES || characters.size < MAX_CHARACTERS) {
                        page++
                        viewModel.page = page
                        viewModel.setStateEvent(CharacterStateEvent.GetCharacterEvent)

                        Log.d(TAG, "Should call for more")

                    } else {
                        Log.d(TAG, "no need to call")
                    }

                }
            }
        }
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = characterAdapter
        filter_edit_text.addTextChangedListener(getTextWatcher())
        filter_edit_text.onFocusChangeListener = getOnFocusChangeListener()
        button.setOnClickListener {
            viewModel.name = "%${filter_edit_text.text}%"
            filter_edit_text.clearFocus()
            viewModel.setStateEvent(CharacterStateEvent.GetFilteredCharacters)
        }
        recyclerView.addOnScrollListener(getOnScrollListener())
    }

    private fun getOnFocusChangeListener(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { v, hasFocus ->
            if (v?.id == R.id.filter_edit_text && !hasFocus) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.name = "%${s.toString()}%"
                viewModel.setStateEvent(CharacterStateEvent.GetFilteredCharacters)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
    }

    private fun getOnItemTouchListener(): OnItemTouchListener {
        return object : OnItemTouchListener {
            override fun onClicked(character: Character) {
                println("Clicked on ${character.name}")
                val bottomSheet = CharacterDetailBottomSheetDialog
                bottomSheet.character = character
                bottomSheet.show(supportFragmentManager, TAG)
            }

        }
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer {
            when (it) {
                is DataState.Success<List<Character>> -> {
                    displayProgressBar(false)
                    println("Got Characters ${it.data}")
                    characters = it.data as ArrayList<Character>
                    if (characters.size == MAX_CHARACTERS) page = MAX_PAGES
                    characterAdapter.submitList(it.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(false)
                }
            }
        })

        viewModel.filteredDataState.observe(this, Observer {
            when (it) {
                is DataState.Success<List<Character>> -> {
                    displayProgressBar(false)
                    println("Got filtered characters ${it.data}")
                    characterAdapter.submitList(it.data)
                    page++
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(false)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        var error = ""
        error = message ?: "Unkown Error"
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progressBar.visibility = if (isDisplayed) VISIBLE else GONE
    }
}
