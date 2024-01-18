package com.example.playlistmaker.mediateca.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "track_in_playlist_table")
data class TrackInPlaylistEntity(

@PrimaryKey
val trackId: Long? = 0,
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










