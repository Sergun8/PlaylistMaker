package com.example.playlistmaker.search.viewModel

import com.example.playlistmaker.search.domain.Track

sealed interface SearchState {
    object Loading : SearchState
    data class SearchContent(val tracks: List<Track>) : SearchState
    data class HistoryContent(val tracks: List<Track>) : SearchState
    data class Error(val errorMessage: String) : SearchState
    data class EmptySearch(val emptySearchMessage: String) : SearchState
    object EmptyScreen : SearchState

}