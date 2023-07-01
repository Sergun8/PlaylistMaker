package com.example.playlistmaker.Player.domain


class PlayerInteractor (val interactor: PlayerInteractorRepository) {

    fun preparePlayer(url: String) {
        interactor.preparePlayer(url)
    }
    fun pausePlayer() {
        interactor.pausePlayer()
    }
    fun startPlayer() {
        interactor.startPlayer()
    }
    fun release () {
        interactor.release()
    }
    fun getPosition () = interactor.getPosition()
}