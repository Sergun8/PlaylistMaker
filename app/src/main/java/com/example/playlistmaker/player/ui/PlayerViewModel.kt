package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel : ViewModel() {

    val mediaPlayerInteractor: PlayerInteractor = Creator.providePlayerInteractor()

   // private val handler = Handler(Looper.getMainLooper())
        //  private lateinit var playerRunnable: Runnable
private lateinit var plaerState: PlayerState
    private lateinit var track: Track
    private val playButtonState = MutableLiveData<Boolean>()
    fun observePlayButtonState(): LiveData<Boolean> = playButtonState

    private val playButtonEnableState = MutableLiveData<Boolean>()
    fun observePlayButtonEnabledState(): LiveData<Boolean> = playButtonEnableState

  //  private val secondsState = MutableLiveData<Long>()
   // fun observeSecondsState(): LiveData<Long> = secondsState


    @SuppressLint("StaticFieldLeak")
    private lateinit var excerptDuration: TextView
   // private val setTimeRunnable = Runnable { setTime() }

    fun preparePlayer(trackPreviewUrl: String) {
        mediaPlayerInteractor.preparePlayer(trackPreviewUrl)
    }


    /*
    override fun onCleared() {
        mediaPlayerInteractor.preparePlayer(track.previewUrl)
        handler.removeCallbacksAndMessages(null)
    }

    fun preparePlayer(state: PlayerState) {
        audioPlayerInteractor.preparePlayer(state) { state ->
            when (state) {
                PlayerState.STATE_PREPARED -> {
                    playButtonEnableState.postValue(true)
                }
                else -> {}
            }
        }
    }
*/
    fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                mediaPlayerInteractor.startPlayer()
                playButtonState.postValue(false)
              //  handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
            }

            PlayerState.STATE_PLAYING -> {
                mediaPlayerInteractor.pausePlayer()
                playButtonState.postValue(true)
               // handler.removeCallbacks(setTimeRunnable)
            }
        }
    }

    fun onStart() {

        mediaPlayerInteractor.startPlayer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
         playButtonState.postValue(false)
       //  handler.removeCallbacksAndMessages(null)
    }

    fun onDestroy() {
        mediaPlayerInteractor.release()
        // handler.removeCallbacks(setTimeRunnable)


    }

    fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        when (plaerState) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                mediaPlayerInteractor.startPlayer()
                playButtonState.postValue(false)
              //  handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
            }

            PlayerState.STATE_PLAYING -> {
                mediaPlayerInteractor.pausePlayer()
                playButtonState.postValue(true)
              //  handler.removeCallbacks(setTimeRunnable)
            }
        }

    }

    private fun setTime() {
        excerptDuration.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayerInteractor.getPosition())
       // handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    /*
        private fun startTimer(duration: Long) {
            val startTime = System.currentTimeMillis()
            playerRunnable = createUpdateTimerTask(startTime, duration * DELAY_MS)
            handler.post(playerRunnable)
        }

        private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
            return object : Runnable {
                override fun run() {
                    // Сколько прошло времени с момента запуска таймера
                    val elapsedTime = System.currentTimeMillis() - startTime
                    // Сколько осталось до конца
                    val remainingTime = duration - elapsedTime

                    if (remainingTime > 0) {
                        // Если всё ещё отсчитываем секунды —
                        // обновляем UI и снова планируем задачу
                        secondsState.postValue(remainingTime / DELAY_MS)
                        handler.postDelayed(this, DELAY_MS)
                    } else {
                        pausePlayer()
                    }
                }
            }
        }
    */
    companion object {
        private const val DELAY_MS = 1000L
        const val SET_TIME_DELAY = 400L
    }
}