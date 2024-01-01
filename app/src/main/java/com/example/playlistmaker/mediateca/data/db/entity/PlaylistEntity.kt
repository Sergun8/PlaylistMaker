package com.example.playlistmaker.mediateca.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist_table")
data class PlaylistEntity(

    @PrimaryKey(autoGenerate = true)
    val playlistId: Long? = 0,
    val playlistName: String = "",
    val description: String?,
    val preview:String?,
    val amountTrack: Long,
    val trackIds: String
)
