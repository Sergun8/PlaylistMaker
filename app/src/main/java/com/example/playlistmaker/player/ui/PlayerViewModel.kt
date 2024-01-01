package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.domain.PlaylistInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class PlayerViewModel(val mediaPlayerInteractor: PlayerInteractor, val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var timerJob: Job? = null
    private var isPlayerCreated = false
    private var isFavorite = false
    private val playState = MutableLiveData<PlayerState>()
    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun observePlayState(): LiveData<PlayerState> = playState

    private val durationState = MutableLiveData<String>()
    fun observeDurationState(): LiveData<String> = durationState

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    private var playlistLiveData = MutableLiveData<List<Playlist>>()
    fun playlistLiveData(): LiveData<List<Playlist>> = playlistLiveData

    private var addTrackInPlaylistData = MutableLiveData<Boolean>()
    fun addTrackInPlaylistLiveData(): LiveData<Boolean> = addTrackInPlaylistData
    fun preparePlayer(previewUrl: String) {
        if (!isPlayerCreated) {
            mediaPlayerInteractor.preparePlayer(previewUrl)
            timerJob?.cancel()
            updatePlayerState()
        } else return
    }

    private fun updatePlayerState() {
        mediaPlayerInteractor.setOnStateChangeListener { state ->
            if (state == PlayerState.STATE_PREPARED) {
                isPlayerCreated = true
            }
            if (state == PlayerState.STATE_COMPLETE) {
                timerJob?.cancel()
            }
            playState.value = state
        }
    }

    fun onStart() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.startPlayer()
            updatePlayerState()
            setTime()
        } else return
    }

    fun onPause() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.pausePlayer()
            updatePlayerState()
            timerJob?.cancel()
        } else return
    }

    fun onDestroy() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.release()
            isPlayerCreated = false
        } else return
        timerJob?.cancel()
    }

    private fun setTime() {

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(SET_TIME_DELAY)
                durationState.postValue(
                    timeFormat.format(
                        mediaPlayerInteractor.getPosition()
                    )
                )
            }
        }
    }

    fun checkIsFavourite(track: Track) {
        viewModelScope.launch {
            mediaPlayerInteractor
                .isFavorite(track)
                .collect { isFavorite ->
                    this@PlayerViewModel.isFavorite = isFavorite
                    isFavoriteLiveData.postValue(isFavorite)
                }
        }
    }


    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            isFavorite = if (isFavorite) {
                mediaPlayerInteractor.delLike(track)
                isFavoriteLiveData.postValue(false)
                false
            } else {
                mediaPlayerInteractor.setLike(track)
                isFavoriteLiveData.postValue(true)
                true
            }
        }
    }
/*
    fun addPlaylistClicked(playlist: Playlist){
        val listType: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        var tracks: ArrayList<Track>? = Gson().fromJson(playlist.trackAdd, listType)
        if (tracks?.contains(actualTrack) == true) {
            message.postValue("Трек уже добавлен в плейлист ${playlist.playlistName}")
        }
        else {
            viewModelScope.launch {
                playlistInteractor.deletePlaylist(playlist)
            }
            if (tracks != null) {
                tracks.add(actualTrack)
            } else {
                tracks = ArrayList<Track>().apply { add(actualTrack) }
            }
            val newString = Gson().toJson(tracks)
            val newPlaylist = Playlist(playlist.playlistName, playlist.playlistDescription, playlist.imageUri,
                playlist.trackAmount?.plus(1), newString)
            viewModelScope.launch {
                playlistInteractor.addPlaylist(newPlaylist)
                fillData()
            }
            message.postValue("Добавлено в плейлист ${playlist.playlistName}")


        }
    }
*/
    private val stateLiveData = MutableLiveData<List<Playlist>>()

    fun observeState(): LiveData<List<Playlist>> = stateLiveData
/*
    fun fillData() {
        viewModelScope.launch {
            mediaPlayerInteractor
                .historyPlaylists()
                .collect { playlist ->
                    stateLiveData.postValue(playlist)
                }
        }
    }
*/


    fun getPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { listPlaylist ->
                playlistLiveData.value = listPlaylist
            }
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        val isAddTrack = playlist.trackIds.contains(track.trackId.toLong())
        if (isAddTrack) addTrackInPlaylistData.value = true
        else {
            viewModelScope.launch {
                mediaPlayerInteractor.addTrackInPlaylist(playlist, track)
            }
            addTrackInPlaylistData.value = false
        }
    }

    companion object {
        const val SET_TIME_DELAY = 300L
    }

}