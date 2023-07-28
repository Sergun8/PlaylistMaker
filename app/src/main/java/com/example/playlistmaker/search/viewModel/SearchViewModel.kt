package com.example.playlistmaker.search.viewModel

import android.app.Application
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor


class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val interactor = Creator.provideSearchInteractor(application)
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private val clearTextState = MutableLiveData<ClearTextState>(ClearTextState.None)
    fun observeClearTextState(): LiveData<ClearTextState> = clearTextState
    fun textCleared() {
        clearTextState.value = ClearTextState.None
    }

    fun onClearTextPressed() {
        clearTextState.value = ClearTextState.ClearText
        renderState(SearchState.HistoryContent(interactor.readHistory()))
    }

    public override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun onTextChanged(searchText: String?) {
        if (searchText.isNullOrEmpty()) {
            if (interactor.readHistory().isNotEmpty()) renderState(
                SearchState.HistoryContent(
                    interactor.readHistory()
                )
            ) else renderState(SearchState.EmptyScreen)
        } else {
            searchDebounce(searchText)
        }
    }

    fun onClearSearchHistoryPressed() {
        interactor.clearHistory()
        renderState(SearchState.EmptyScreen)
    }

    fun onRefreshSearchButtonPressed(searchRequest: String) {
        searchRequest(searchRequest)
    }

    fun onTrackPressed(track: Track) {
        interactor.addTrackHistory(track)
    }

    fun onFocusChanged(hasFocus: Boolean, searchText: String) {
        if (hasFocus && searchText.isEmpty() && interactor.readHistory().isNotEmpty()) {
            renderState(SearchState.HistoryContent(interactor.readHistory()))
        }
    }

   private fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }


    private fun searchRequest(searchText: String) {

        if (searchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            interactor.search(searchText, object : SearchInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    when {
                        errorMessage != null -> {
                            renderState(SearchState.Error)
                        }
                        foundTracks.isNullOrEmpty() -> {
                            renderState(SearchState.EmptySearch)
                        }
                        else -> {
                            renderState(
                                SearchState.SearchContent(
                                    tracks =  foundTracks
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }

    }
}