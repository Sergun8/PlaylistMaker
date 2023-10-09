package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun pausePlayer()
    fun startPlayer()
    fun release()
    fun getPosition(): Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}

