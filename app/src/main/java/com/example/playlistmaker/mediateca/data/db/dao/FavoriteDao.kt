package com.example.playlistmaker.mediateca.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mediateca.data.db.entity.TrackEntity

@Dao
interface FavoriteDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_table")
    suspend fun getFavoriteTrack(): List<TrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_table WHERE trackId LIKE :trackId)")
    suspend fun isFavorite(trackId: String): Boolean

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTrack(trackEntity: TrackEntity)
}
