package com.example.playlistmaker.mediateca.ui

import com.example.playlistmaker.mediateca.domain.Playlist

sealed interface PlaylistState {
    object Loading : PlaylistState
    data class PlaylistContent(val playlists: List<Playlist>) : PlaylistState
    object Empty : PlaylistState
}