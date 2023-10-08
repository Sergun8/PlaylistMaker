package com.example.playlistmaker.player.ui


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

    private val setTimeRunnable = Runnable { setTime() }

    fun playbackControl() {
        when (playState.value) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_COMPLETE -> {
                onStart()
            }

            PlayerState.STATE_PLAYING -> {
                onPause()
            }

            null -> return
        }
    }

    fun preparePlayer(previewUrl: String) {
        mediaPlayerInteractor.preparePlayer(previewUrl)
        updatePlayerState()
    }

    fun updatePlayerState() {
        mediaPlayerInteractor.setOnStateChangeListener { state ->
            playState.value = state
        }
    }

    fun onStart() {
        mediaPlayerInteractor.startPlayer()
        updatePlayerState()
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    fun onPause() {
        mediaPlayerInteractor.pausePlayer()
        updatePlayerState()
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