package com.example.playlistmaker.search.ui.viewModel

sealed interface ClearTextState {
    object None : ClearTextState
    object ClearText : ClearTextState
}