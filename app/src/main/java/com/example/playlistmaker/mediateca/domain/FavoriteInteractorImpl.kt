package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteTrackRepository: FavoriteTrackRepository) :
    FavoriteInteractor {
    override fun getFavoriteTrack(): Flow<List<Track>> {
        return favoriteTrackRepository.getFavoriteTracks()
    }
}