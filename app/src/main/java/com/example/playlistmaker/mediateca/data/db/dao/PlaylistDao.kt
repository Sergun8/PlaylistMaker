package com.example.playlistmaker.mediateca.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity



@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity:  PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE PlaylistId = :id")
    suspend fun getCurrentPlaylist(id: Long): PlaylistEntity
    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
    @Query("UPDATE playlist_table SET playlistName=:playlistName, description=:description, preview=:preview WHERE playlistId=:id")
    suspend fun updatePlaylistInfo(
        id: Long,
        playlistName: String,
        description: String,
        preview: String
    )
    @Query ("DELETE FROM playlist_table WHERE PlaylistId = :id")
    suspend fun deletePlaylistEntity(id: Long)
}