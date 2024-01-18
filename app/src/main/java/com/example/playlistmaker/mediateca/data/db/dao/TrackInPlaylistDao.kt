package com.example.playlistmaker.mediateca.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mediateca.data.db.entity.TrackInPlaylistEntity



    @Dao
    interface TrackInPlaylistDao {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun addTrackInPlaylist(trackInPlaylistEntity: TrackInPlaylistEntity)

        @Query ("DELETE FROM track_in_playlist_table WHERE TrackId = :id")
        suspend fun deleteTrackFromPlaylist(id: String)

        @Query("SELECT * FROM track_in_playlist_table WHERE trackId = :id")
        suspend fun getCurrentListTrack(id: String): TrackInPlaylistEntity
    }

