package com.example.playlistmaker.player.domain

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
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
}