package com.example.playlistmaker.search.viewModel

sealed interface ClearTextState {
    object None : ClearTextState
    object ClearText : ClearTextState
}