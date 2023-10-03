package com.example.playlistmaker.search.data.localStorage

import com.example.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun read(): ArrayList<Track>
    fun write(track: Track)
    fun clear()
}
