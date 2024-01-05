package com.example.playlistmaker.mediateca.domain


import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackInPlaylist(playlist: Playlist, track: Track)
    fun getListTrack(trackList: List<Long>): Flow<List<Track>>
    suspend fun editPlaylist(playlist: Playlist)

}

