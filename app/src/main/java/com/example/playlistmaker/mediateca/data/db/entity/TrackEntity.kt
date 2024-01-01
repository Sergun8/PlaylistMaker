package com.example.playlistmaker.mediateca.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class TrackEntity(

    @PrimaryKey
    val trackId: String = "",
    val trackName: String = "",
    val artistName: String ="",
    val trackTimeMillis: String? = null,
    val artworkUrl100: String = "",
    val collectionName: String? = null,
    val country: String = "",
    val primaryGenreName: String ="",
    val releaseDate: String= "",
    val previewUrl: String = "",
    val isFavorite: Boolean = false,
    val date: Long = 0L
)