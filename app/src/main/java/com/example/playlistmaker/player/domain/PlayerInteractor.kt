package com.example.playlistmaker.player.domain

import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun pausePlayer()
    fun startPlayer()
    fun release()
    fun getPosition(): Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    suspend fun isFavorite(track: Track): Flow<Boolean>
    suspend fun delLike(track: Track)
    suspend fun setLike(track: Track)
    suspend fun addTrackInPlaylist(playlist: Playlist, track: Track)
    fun getListTrack(trackList: List<Long>): Flow<List<Track>>
    suspend fun editPlaylist(playlist: Playlist)
}

