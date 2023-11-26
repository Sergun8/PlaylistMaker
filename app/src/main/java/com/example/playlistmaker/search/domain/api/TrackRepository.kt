package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.Resource
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun search(expression: String): Flow<Resource<List<Track>>>
    fun readHistory(): ArrayList<Track>
    fun clearHistory()
    fun addTrackHistory(track: Track)
}
