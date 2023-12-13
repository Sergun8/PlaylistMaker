package com.example.playlistmaker.mediateca.data

import com.example.playlistmaker.mediateca.data.db.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateca.data.db.entity.TrackEntity
import com.example.playlistmaker.mediateca.domain.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTrackRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoriteTrackDao().getFavoriteTrack().reversed()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map(trackDbConvertor::map)
    }
    override suspend fun isFavorite(track: Track): Flow<Boolean> = flow {
      emit(appDatabase.favoriteTrackDao().isFavorite(track.trackId))
    }
    override suspend fun delLike(track: Track){
        appDatabase.favoriteTrackDao().deleteFavoriteTrack(trackDbConvertor.map(track))
    }
    override suspend fun addLike(track: Track){
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.favoriteTrackDao().addFavoriteTrack(trackEntity)
    }
}