package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val mediaPlayerInteractor: PlayerInteractor) : ViewModel() {

    private var timerJob: Job? = null
    private var isPlayerCreated = false
    private val playState = MutableLiveData<PlayerState>()
    fun observePlayState(): LiveData<PlayerState> = playState

    private val durationState = MutableLiveData<String>()
    fun observeDurationState(): LiveData<String> = durationState

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
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                        mediaPlayerInteractor.getPosition()
                    )
                )
            }
        }
    }

    companion object {
        const val SET_TIME_DELAY = 300L
    }

}