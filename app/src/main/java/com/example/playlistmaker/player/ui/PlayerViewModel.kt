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
    private val playState = MutableLiveData<PlayerState>()
    fun observePlayState(): LiveData<PlayerState> = playState

    private val durationState = MutableLiveData<String>()
    fun observeDurationState(): LiveData<String> = durationState


    @SuppressLint("StaticFieldLeak")

    val setTimeRunnable = Runnable { setTime() }

    fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                mediaPlayerInteractor.startPlayer()
                playState.postValue(PlayerState.STATE_PLAYING)
                handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
            }
            PlayerState.STATE_PLAYING -> {
                mediaPlayerInteractor.pausePlayer()
                playState.postValue(PlayerState.STATE_PAUSED)
                handler.removeCallbacks(setTimeRunnable)
            }
        }
    }

    fun preparePlayer(previewUrl: String) {
        mediaPlayerInteractor.preparePlayer(previewUrl)
    }

    fun onStart() {
        mediaPlayerInteractor.startPlayer()
    }

    fun onPause() {
        mediaPlayerInteractor.pausePlayer()
        handler.removeCallbacksAndMessages(null)
    }

    fun onDestroy() {
        mediaPlayerInteractor.release()
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