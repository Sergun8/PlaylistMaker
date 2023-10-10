package com.example.playlistmaker.search.ui.viewModel


import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.models.ErrorNetwork


class SearchViewModel(
    private val interactor: SearchInteractor
) : ViewModel() {

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
                override fun consume(foundTracks: List<Track>?, errorMessage: ErrorNetwork?) {
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
                                    tracks = foundTracks
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



    }
}