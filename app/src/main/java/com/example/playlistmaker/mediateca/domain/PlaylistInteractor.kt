package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
   // suspend fun editPlaylist(playlist: Playlist)

}





