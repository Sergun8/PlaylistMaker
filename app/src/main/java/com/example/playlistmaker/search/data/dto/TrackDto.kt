package com.example.playlistmaker.search.data.dto

data class TrackDto (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String?,
    val artworkUrl100: String,
    val collectionName: String?,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val previewUrl: String
)