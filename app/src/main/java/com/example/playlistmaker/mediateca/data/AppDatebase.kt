package com.example.playlistmaker.mediateca.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateca.data.db.dao.FavoriteDao
import com.example.playlistmaker.mediateca.data.db.dao.PlaylistDao
import com.example.playlistmaker.mediateca.data.db.dao.TrackInPlaylistDao
import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateca.data.db.entity.TrackEntity
import com.example.playlistmaker.mediateca.data.db.entity.TrackInPlaylistEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistsDao(): TrackInPlaylistDao

}