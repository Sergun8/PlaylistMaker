package com.example.playlistmaker.mediateca.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.setting.domain.sharing.api.ExternalNavigator
import kotlinx.coroutines.flow.Flow

class InfoPlaylistInteractorImpl(private val repository: PlaylistRepository, private val navigator: ExternalNavigator): InfoPlaylistInteractor {
    override suspend fun getInfoPlaylist(playListId: Long): Flow<Playlist> {
        return repository.getInfoPlaylist(playListId)
    }
    override suspend fun getListTrack(trackIds: List<Long>) : Flow<List<Track>> {
        return repository.getListTrack(trackIds)
    }
    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Long) {
        repository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override fun sharePlaylist(sharePlaylist: String) {
        navigator.sharePlaylist(sharePlaylist = sharePlaylist)
    }
    override suspend fun delPlaylist( playlistId: Long) {
        repository.delPlaylist( playlistId)
    }
}