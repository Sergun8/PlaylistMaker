package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val mediaPlayerInteractor: PlayerInteractor) : ViewModel() {


    private val handler = Handler(Looper.getMainLooper())
    private var isPlayerCreated = false
    private val playState = MutableLiveData<PlayerState>()
    fun observePlayState(): LiveData<PlayerState> = playState

    private val durationState = MutableLiveData<String>()
    fun observeDurationState(): LiveData<String> = durationState


    @SuppressLint("StaticFieldLeak")

    val setTimeRunnable = Runnable { setTime() }
    fun preparePlayer(previewUrl: String) {
        if (!isPlayerCreated) {
            mediaPlayerInteractor.preparePlayer(previewUrl)
            handler.removeCallbacks(setTimeRunnable)
            updatePlayerState()
        } else return
    }

    private fun updatePlayerState() {
        mediaPlayerInteractor.setOnStateChangeListener { state ->
            if (state == PlayerState.STATE_PREPARED) {
                isPlayerCreated = true
            }
            if (state == PlayerState.STATE_COMPLETE) {
                handler.removeCallbacksAndMessages(null)
            }
            playState.value = state
        }
    }

    fun onStart() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.startPlayer()
            updatePlayerState()
            handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
        } else return
    }

    fun onPause() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.pausePlayer()
            updatePlayerState()
            handler.removeCallbacksAndMessages(null)
        } else return
    }

    fun onDestroy() {
        if (isPlayerCreated) {
            mediaPlayerInteractor.release()
            isPlayerCreated = false
        } else return
        handler.removeCallbacks(setTimeRunnable)
    }

    private fun setTime() {
        durationState.postValue(
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                mediaPlayerInteractor.getPosition()
            )
        )
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    companion object {
        const val SET_TIME_DELAY = 400L
    }

}