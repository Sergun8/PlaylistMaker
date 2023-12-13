package com.example.playlistmaker.mediateca.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface FavoriteState {
    object Loading : FavoriteState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    object Empty : FavoriteState
}