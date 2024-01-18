package com.example.playlistmaker.mediateca.ui.viewModel

import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.search.domain.models.Track

sealed interface InfoPlaylistState {
    object Empty : InfoPlaylistState
    data class Content(
        val playlist: Playlist, val tracks: List<Track>
    ) : InfoPlaylistState

}