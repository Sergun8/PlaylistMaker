package com.example.playlistmaker.search.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.models.ErrorNetwork
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val interactor: SearchInteractor
) : ViewModel() {

    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private val clearTextState = MutableLiveData<ClearTextState>(ClearTextState.None)
    fun observeClearTextState(): LiveData<ClearTextState> = clearTextState
    fun textCleared() {
        clearTextState.value = ClearTextState.None
        searchJob?.cancel()
    }

    fun onClearTextPressed() {
        clearTextState.value = ClearTextState.ClearText
        renderState(SearchState.HistoryContent(interactor.readHistory()))
    }

    public override fun onCleared() {
        super.onCleared()
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
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(searchText: String) {

        if (searchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                interactor
                    .search(searchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }


    private fun processResult(searchTracks: List<Track>?, errorMessage: ErrorNetwork?) {
        val tracks = mutableListOf<Track>()
        if (searchTracks != null) {
            tracks.addAll(searchTracks)
        }

        when {
            errorMessage != null -> {
                renderState(SearchState.Error)
            }

            tracks.isEmpty() -> {
                renderState(SearchState.EmptySearch)
            }

            else -> {
                renderState(SearchState.SearchContent(tracks = tracks))
            }
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        // private val SEARCH_REQUEST_TOKEN = Any()
    }
}