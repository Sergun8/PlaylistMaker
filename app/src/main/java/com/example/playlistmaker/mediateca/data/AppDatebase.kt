package com.example.playlistmaker.mediateca.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateca.data.db.dao.FavoriteDao
import com.example.playlistmaker.mediateca.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteTrackDao(): FavoriteDao
}