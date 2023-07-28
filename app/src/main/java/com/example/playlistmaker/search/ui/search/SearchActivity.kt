package com.example.playlistmaker.search.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.viewModel.ClearTextState
import com.example.playlistmaker.search.viewModel.SearchState
import com.example.playlistmaker.search.viewModel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private var countValue = ""
    private val trackList = ArrayList<Track>()
    private val historyList = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var inputEditText: EditText
    private lateinit var rvTrack: RecyclerView
    private lateinit var clearButton: ImageView
    private lateinit var notFound: String
    lateinit var noConnection: String
    private lateinit var updateButton: Button
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var historyView: LinearLayout
    private lateinit var rvHistoryList: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var searchHistory: SharedPreferencesClient
    private lateinit var yuoSearch: TextView
    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }
        if (savedInstanceState != null) {
            inputEditText.text = savedInstanceState.getCharSequence(SEARCH_VALUE) as Editable
        }

        findViewById<RecyclerView?>(R.id.history_search_list).apply {
            adapter = historyAdapter
        }

        val toolbarSearchActivity =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbars)
        toolbarSearchActivity.setNavigationOnClickListener { finish() }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        viewModel.observeClearTextState().observe(this) { clearTextState ->
            if (clearTextState is ClearTextState.ClearText) {
                clearSearchText()
                hideKeyboard()
                viewModel.textCleared()
            }
        }

        clearButton.setOnClickListener {
            viewModel.onClearTextPressed()
        }

        updateButton.setOnClickListener {
            viewModel.onRefreshSearchButtonPressed(countValue)
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus, inputEditText.text.toString())
        }

        clearHistory.setOnClickListener {
            viewModel.onClearSearchHistoryPressed()
        }
    }


    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    private fun initViews() {
        inputEditText = findViewById(R.id.editTextSearch)
        clearButton = findViewById(R.id.clearIcon)
        rvTrack = findViewById(R.id.recyclerViewTrack)
        updateButton = findViewById(R.id.search_update_button)
        errorImage = findViewById(R.id.search_error_image)
        errorText = findViewById(R.id.search_error_text)
        notFound = getString(R.string.nothing_was_found)
        noConnection = getString(R.string.communication_problems)
        historyView = findViewById(R.id.search_history)
        clearHistory = findViewById(R.id.clear_history)
        rvHistoryList = findViewById(R.id.history_search_list)
        yuoSearch = findViewById(R.id.you_searched)

        adapter = TrackAdapter {
            if (clickDebounce()) {
                viewModel.onTrackPressed(it)
                val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    .apply {
                        putExtra(TRACK, Gson().toJson(it))
                    }
                startActivity(displayIntent)
            }
        }
        historyAdapter = TrackAdapter {
            if (clickDebounce()) {
                viewModel.onTrackPressed(it)
                val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    .apply {
                        putExtra(TRACK, Gson().toJson(it))
                    }

                startActivity(displayIntent)
            }


        }






        adapter.trackList = trackList
        rvTrack.adapter = adapter
        historyAdapter.trackList = historyList
        rvHistoryList.adapter = historyAdapter

        progressBar = findViewById(R.id.progressBar)

    }

    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            viewModel.onTextChanged(s.toString() ?: "")
        }

        override fun afterTextChanged(editable: Editable?) {
            if (inputEditText.text.isEmpty()) {
                render(SearchState.HistoryContent(historyList))
            } else {
                historyView.visibility = GONE
                clearButton.visibility = VISIBLE
            }

            clearHistory.visibility =
                if (historyList.isEmpty()) GONE
                else VISIBLE
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.SearchContent -> showSearchResult(state.tracks)
            is SearchState.HistoryContent -> historyListVisibility(state.tracks)
            is SearchState.EmptySearch -> showNotFound()
            is SearchState.Error -> showNotConnected()
            is SearchState.EmptyScreen -> showEmptyScreen()
        }
    }

    private fun showLoading() {
        progressBar.visibility = VISIBLE
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    private fun showEmptyScreen() {
        rvTrack.visibility = GONE
        rvHistoryList.visibility = GONE
        progressBar.visibility = GONE
        errorImage.visibility = GONE
        errorText.visibility = GONE
        updateButton.visibility = GONE
        historyView.visibility = GONE
        clearHistory.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(tracks: List<Track>) {
        showEmptyScreen()
        rvTrack.visibility = VISIBLE
        trackList.clear()
        trackList.addAll(tracks)
        adapter.trackList = trackList
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun historyListVisibility(searchHistory: List<Track>) {
        showEmptyScreen()
        rvHistoryList.visibility = VISIBLE
        historyView.visibility = VISIBLE
        yuoSearch.visibility = VISIBLE
        clearHistory.visibility = VISIBLE
        historyAdapter.trackList = searchHistory as ArrayList<Track>
        historyAdapter.notifyDataSetChanged()
    }



    private fun showNotFound() {
        showEmptyScreen()
        errorText.text = getString(R.string.nothing_was_found)
        errorText.visibility = VISIBLE
        errorImage.visibility = VISIBLE
        errorImage.setImageResource(R.drawable.ic_nothing_found)
        progressBar.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showNotConnected() {
        showEmptyScreen()
        errorImage.visibility = VISIBLE
        errorText.visibility = VISIBLE
        updateButton.visibility = VISIBLE
        errorImage.setImageResource(R.drawable.ic_not_internet)
        errorText.text = noConnection
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
        updateButton.setOnClickListener { viewModel.onRefreshSearchButtonPressed(inputEditText.text.toString()) }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, countValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        countValue = savedInstanceState.getString(SEARCH_VALUE, "")
    }

    override fun onStop() {
        super.onStop()
        viewModel.onCleared()
    }

    private fun clearSearchText() {
        inputEditText.text.clear()
        clearButton.visibility = GONE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SHARED_PREFS = "SHARED_PREFS"
        const val NIGHT_THEME = "NIGHT_THEME"
        const val TRACK = "TRACK"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}