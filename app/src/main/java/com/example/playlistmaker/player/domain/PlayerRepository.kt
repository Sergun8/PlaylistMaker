package com.example.playlistmaker.player.domain

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition(): Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}
