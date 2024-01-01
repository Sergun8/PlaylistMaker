package com.example.playlistmaker.mediateca.data

import com.example.playlistmaker.mediateca.data.db.convertors.PlaylistDbConvertor
import com.example.playlistmaker.mediateca.data.db.convertors.TrackInPlaylistDbConvertor
import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateca.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.domain.PlaylistRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackInPlaylistDbConvertor: TrackInPlaylistDbConvertor
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val tracks = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(tracks))
    }

    override suspend fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        appDatabase.trackInPlaylistsDao().addTrackInPlaylist(trackInPlaylistDbConvertor.map(track))
        addTrackPlaylist(playlist.playlistId!!, track.trackId)
    }

    override fun getListTrack(trackList: List<Long>): Flow<List<Track>> = flow {
        emit(newListTrack(trackList as MutableList<Long>))
    }


    override suspend fun editPlaylist(playlist: Playlist) {
        updatePlaylist(playlist)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map(playlistDbConvertor::map)
    }

    private suspend fun newListTrack(trackList: List<Long>): List<Track> {
        val trackListString = trackList.map { trackId -> trackId.toString() }
        val newTrackEntityList = mutableListOf<TrackInPlaylistEntity>()
        trackListString.indices.forEach { trackId ->
            newTrackEntityList.run {
                add(appDatabase.trackInPlaylistsDao().getCurrentListTrack(trackListString[trackId]))
            }
        }
        return newTrackEntityList.map { playlistTrackEntity ->
            trackInPlaylistDbConvertor.map(
                playlistTrackEntity
            )
        }
    }

    private suspend fun addTrackPlaylist(playlistId: Long, trackId: String) {
        val updatedPlaylist =
            playlistDbConvertor.map(appDatabase.playlistDao().getCurrentPlaylist(playlistId))
        updatedPlaylist.trackIds.add(0, trackId.toLong())
        updatedPlaylist.amountTrack++
        updatePlaylist(updatedPlaylist)
    }

    private suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }
}
