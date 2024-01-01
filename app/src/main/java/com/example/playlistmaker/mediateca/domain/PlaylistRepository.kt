package com.example.playlistmaker.mediateca.domain

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackInPlaylist(playlist: Playlist, track: Track)
    fun getListTrack(trackList: List<Long>): Flow<List<Track>>
  //  suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int): List<Long>
  //  suspend fun deletePlaylist(playlist: Playlist)
    suspend fun editPlaylist(playlist: Playlist)

}

