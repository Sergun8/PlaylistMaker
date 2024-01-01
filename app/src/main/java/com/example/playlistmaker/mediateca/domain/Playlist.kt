package com.example.playlistmaker.mediateca.domain



data class Playlist(
    val playlistId: Long? = 0,
    val playlistName: String = "",
    val description: String?,
    val preview: String?,
    var amountTrack: Long = 0,
    val trackIds: MutableList<Long> = arrayListOf()
)

