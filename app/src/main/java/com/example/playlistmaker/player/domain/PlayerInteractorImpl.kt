package com.example.playlistmaker.player.domain

import com.example.playlistmaker.mediateca.domain.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val repository: PlayerRepository,
    private val favoriteTrackRepository: FavoriteTrackRepository
) : PlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun release() {
        repository.release()
    }

    override fun getPosition() = repository.getPosition()
    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }

    override suspend fun setLike(track: Track) {
        favoriteTrackRepository.addLike(track)
    }

    override suspend fun isFavorite(track: Track): Flow<Boolean> {
        return favoriteTrackRepository.isFavorite(track)
    }

    override suspend fun delLike(track: Track) {
        favoriteTrackRepository.delLike(track)
    }

}