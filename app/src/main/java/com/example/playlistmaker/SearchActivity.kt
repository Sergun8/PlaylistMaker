package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {

    private var countValue = ""
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()
    private val itunesService = retrofit.create(ItunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private val historyList = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var inputEditText: EditText
    private lateinit var rvTrack: RecyclerView
    private lateinit var clearButton: ImageView
    private lateinit var notFound: String
    private lateinit var noConnection: String
    private lateinit var updateButton: Button
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var historyView: LinearLayout
    private lateinit var rvHistoryList: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var yuoSearch: TextView
    private lateinit var progressBar: ProgressBar
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        val toolbarSearchActivity =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbars)
        toolbarSearchActivity.setNavigationOnClickListener { finish() }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(currentFocus ?: View(this))
            trackList.clear()
            errorImage.visibility = GONE
            errorText.visibility = GONE
            updateButton.visibility = GONE
            adapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchDebounce()
                if (inputEditText.text.isEmpty()) {
                    errorImage.visibility = GONE
                    errorText.visibility = GONE
                    updateButton.visibility = GONE
                    rvTrack.visibility = GONE
                    historyView.visibility = VISIBLE
                } else historyView.visibility = GONE

                clearHistory.visibility =
                    if (historyList.isEmpty()) GONE
                    else VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                false
            } else true
        }
        historyList.addAll(searchHistory.readHistory())

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            historyListVisibility(hasFocus && inputEditText.text.isEmpty() && historyList.isNotEmpty())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
        searchHistory = SearchHistory(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))
        yuoSearch = findViewById(R.id.you_searched)

        adapter = TrackAdapter {
            addTrackHistory(it)
            if (clickDebounce()) startPlayer(it)
        }
        adapter.trackList = trackList
        rvTrack.adapter = adapter
        historyAdapter = TrackAdapter {
            addTrackHistory(it)
            startPlayer(it)
        }

        historyAdapter.trackList = historyList
        rvHistoryList.adapter = historyAdapter
        clearHistory.setOnClickListener {
            historyList.clear()
            rvHistoryList.adapter?.notifyDataSetChanged()
            historyListVisibility(historyList.isNotEmpty())
        }
        progressBar = findViewById(R.id.progressBar)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String) {
        progressBar.visibility = GONE
        when (text) {
            notFound -> {
                errorImage.visibility = VISIBLE
                errorText.visibility = VISIBLE
                updateButton.visibility = GONE
                trackList.clear()
                errorImage.setImageResource(R.drawable.ic_nothing_found)
                errorText.text = text
                adapter.notifyDataSetChanged()
            }
            noConnection -> {
                errorImage.visibility = VISIBLE
                errorText.visibility = VISIBLE
                updateButton.visibility = VISIBLE
                trackList.clear()
                rvTrack.visibility = GONE
                errorImage.setImageResource(R.drawable.ic_not_internet)
                errorText.text = text
                adapter.notifyDataSetChanged()
                updateButton.setOnClickListener { search() }
            }
            else -> {
                errorImage.visibility = GONE
                errorText.visibility = GONE
                updateButton.visibility = GONE
            }
        }
    }


    private fun search() {
        errorImage.visibility = GONE
        errorText.visibility = GONE
        updateButton.visibility = GONE
        rvTrack.visibility = GONE
        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility = VISIBLE
            itunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            rvTrack.visibility = VISIBLE
                            errorImage.visibility = GONE
                            errorText.visibility = GONE
                            updateButton.visibility = GONE
                            progressBar.visibility = GONE
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                showMessage(notFound)
                            }
                        } else {
                            showMessage(noConnection)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(noConnection)
                    }
                })
        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, countValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        countValue = savedInstanceState.getString(SEARCH_VALUE, "")
    }

    private fun historyListVisibility(yes: Boolean) {
        if (yes) {
            yuoSearch.visibility = VISIBLE
            historyView.visibility = VISIBLE
            errorImage.visibility = GONE
            errorText.visibility = GONE
            updateButton.visibility = GONE
        } else {
            historyView.visibility = GONE
            yuoSearch.visibility = GONE
        }
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveHistory(historyList)
    }

    private fun startPlayer(track: Track) {
        val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            .apply {
                putExtra(TRACK, Gson().toJson(track))
            }
        startActivity(displayIntent)
    }

    private fun addTrackHistory(track: Track) = when {

        historyList.contains(track) -> {
            val position = historyList.indexOf(track)
            historyList.remove(track)
            rvHistoryList.adapter?.notifyItemRemoved(position)
            rvHistoryList.adapter?.notifyItemRangeChanged(position, historyList.size)
            historyList.add(0, track)
            rvHistoryList.adapter?.notifyItemInserted(0)
            rvHistoryList.adapter?.notifyItemRangeChanged(0, historyList.size)
        }
        historyList.size < 10 -> {
            historyList.add(0, track)
            rvHistoryList.adapter?.notifyItemInserted(0)
            rvHistoryList.adapter?.notifyItemRangeChanged(0, historyList.size)
        }

        else -> {
            historyList.removeAt(9)
            rvHistoryList.adapter?.notifyItemRemoved(9)
            rvHistoryList.adapter?.notifyItemRangeChanged(9, historyList.size)
            historyList.add(0, track)
            rvHistoryList.adapter?.notifyItemInserted(0)
            rvHistoryList.adapter?.notifyItemRangeChanged(0, historyList.size)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SHARED_PREFS = "SHARED_PREFS"
        const val NIGHT_THEME = "NIGHT_THEME"
        const val TRACK = "TRACK"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

