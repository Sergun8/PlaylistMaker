package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.utils.Resource

interface TrackRepository {
    fun search(expression: String): Resource<List<Track>>
    fun readHistory(): ArrayList<Track>
    //fun SaveHistory(track: Track)
    fun clearHistory()
    fun addTrackHistory(track: Track)
}
