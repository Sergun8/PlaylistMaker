package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
        fun getFavoriteTrack(): Flow<List<Track>>

}