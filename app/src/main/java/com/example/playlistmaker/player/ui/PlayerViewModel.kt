package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

class PlayerViewModel(val mediaPlayerInteractor: PlayerInteractor) : ViewModel() {

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

    companion object {
        const val SET_TIME_DELAY = 300L
    }

}