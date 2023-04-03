package com.example.playlistmaker

import android.annotation.SuppressLint


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo

import android.widget.*


import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {

    private var countValue = ""

    val iTunesBaseUrl = "https://itunes.apple.com"
    val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private lateinit var inputEditText: EditText
    private lateinit var rvTrack: RecyclerView
    private lateinit var clearButton: ImageView
    private val adapter = TrackAdapter()
    private val notFound = "Ничего не нашлось"
    private val noConnection =
        "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"

    private lateinit var updateButton: Button
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView


    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        inputEditText = findViewById(R.id.editTextSearch)
        clearButton = findViewById(R.id.clearIcon)
        rvTrack = findViewById(R.id.recyclerViewTrack)
        updateButton = findViewById(R.id.search_update_button)
        errorImage = findViewById(R.id.search_error_image)
        errorText = findViewById(R.id.search_error_text)
        val toolbarSearchActivity =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbars)
        toolbarSearchActivity.setNavigationOnClickListener { finish() }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(currentFocus ?: View(this))
            trackList.clear()
            adapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
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

        adapter.trackList = trackList
        rvTrack.adapter = adapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String) {
        when (text) {
            notFound -> {
                errorImage.visibility = VISIBLE
                errorText.visibility = VISIBLE
                updateButton.visibility = View.GONE
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
                rvTrack.visibility = View.GONE
                errorImage.setImageResource(R.drawable.ic_not_internet)
                errorText.text = text
                adapter.notifyDataSetChanged()
                updateButton.setOnClickListener { search() }
            }
            else -> {
                errorImage.visibility = View.GONE
                errorText.visibility = View.GONE
                updateButton.visibility = View.GONE
            }
        }
    }


    private fun search() {

        if (inputEditText.text.isNotEmpty()) {
            itunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            rvTrack.visibility = VISIBLE
                            errorImage.visibility = View.GONE
                            errorText.visibility = View.GONE
                            updateButton.visibility = View.GONE
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
            View.GONE
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


    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }
}

