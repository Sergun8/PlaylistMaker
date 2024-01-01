package com.example.playlistmaker.mediateca.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity


@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity:  PlaylistEntity)
    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>
    @Query("SELECT * FROM playlist_table WHERE PlaylistId = :id")
    suspend fun getCurrentPlaylist(id: kotlin.Long): PlaylistEntity
    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

}