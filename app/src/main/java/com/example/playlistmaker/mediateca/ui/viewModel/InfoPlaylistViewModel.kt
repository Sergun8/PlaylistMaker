package com.example.playlistmaker.mediateca.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateca.domain.InfoPlaylistInteractor
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class InfoPlaylistViewModel(private val interactor: InfoPlaylistInteractor) : ViewModel() {


    private val _stateLiveData = MutableLiveData<InfoPlaylistState>()
    fun stateLiveData(): LiveData<InfoPlaylistState> = _stateLiveData


    var playlist: Playlist? = null
    var tracks: List<Track> = listOf()
    fun getInfoPlaylist(playlistId: Long) {

        viewModelScope.launch {
            interactor.getInfoPlaylist(playlistId)
                .collect { playList ->
                    playlist = playList
                    interactor.getListTrack(playList.trackIds)
                        .collect {
                            tracks = it
                        }
                }
            processResult(playlist!!, tracks)
        }

    }

    fun deleteTrackFromPlaylist(trackId: String, playlistId: Long) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(trackId, playlistId)
            getInfoPlaylist(playlistId)
        }
    }

    fun sharePlaylist(trackList: List<Track>, playlist: Playlist) {

        if (trackList.isEmpty())
            _stateLiveData.postValue(InfoPlaylistState.Empty)
        else {
            interactor.sharePlaylist(createPlaylist(playlist, trackList))
        }
    }
    fun delPlaylist(playlistId: Long) {
        viewModelScope.launch {
            interactor.delPlaylist(playlistId)
            getInfoPlaylist(playlistId)
        }
    }
    private fun createPlaylist(playlist: Playlist, trackList: List<Track>) : String {
        var sharingText = "${playlist.playlistName}\n${playlist.description}\n${playlist.amountTrack} треков\n"
        for (i in trackList.indices) {
            sharingText += "${i + 1}. ${trackList[i].artistName}, ${trackList[i].trackName} - (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackList[i].trackTimeMillis!!.toLong())})\n"
        }
        return sharingText
    }
    private fun processResult(playlist: Playlist, tracks: List<Track>) {

        _stateLiveData.postValue(InfoPlaylistState.Content(playlist, tracks))

    }

}