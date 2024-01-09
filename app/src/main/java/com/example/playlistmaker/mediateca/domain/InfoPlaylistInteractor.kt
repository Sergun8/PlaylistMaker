package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface InfoPlaylistInteractor {
    suspend fun getInfoPlaylist(playListId: Long): Flow<Playlist>
    suspend fun getListTrack(trackIds: MutableList<Long>) : Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Long)
    suspend fun delPlaylist(playlistId: Long)
    fun sharePlaylist(sharePlaylist: String)
}