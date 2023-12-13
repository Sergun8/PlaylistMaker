package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun isFavorite(track: Track): Flow<Boolean>
    suspend fun delLike(track: Track)
    suspend fun addLike(track: Track)
}