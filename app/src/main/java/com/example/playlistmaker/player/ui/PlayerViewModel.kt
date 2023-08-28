package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    val mediaPlayerInteractor: PlayerInteractor = Creator.providePlayerInteractor()

    private val handler = Handler(Looper.getMainLooper())
    private val playButtonState = MutableLiveData<Boolean>()
    fun observePlayButtonState(): LiveData<Boolean> = playButtonState

    private val durationState = MutableLiveData<String>()
    fun observeDurationState(): LiveData<String> = durationState

    @SuppressLint("StaticFieldLeak")

    val setTimeRunnable = Runnable { setTime() }

    fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                mediaPlayerInteractor.startPlayer()
                playButtonState.postValue(false)
                handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
            }
            PlayerState.STATE_PLAYING -> {
                mediaPlayerInteractor.pausePlayer()
                playButtonState.postValue(true)
                handler.removeCallbacks(setTimeRunnable)
            }
        }
    }
    fun onStart() {
        mediaPlayerInteractor.startPlayer()

    }
    fun onPause() {
        mediaPlayerInteractor.pausePlayer()
        playButtonState.postValue(false)
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

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }



}