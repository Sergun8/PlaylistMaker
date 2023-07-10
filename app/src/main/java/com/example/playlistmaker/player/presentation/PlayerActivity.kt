package com.example.playlistmaker.player.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.R.drawable
import com.example.playlistmaker.R.id
import com.example.playlistmaker.R.layout
import com.example.playlistmaker.R.string
import com.example.playlistmaker.search.ui.search.SearchActivity.Companion.TRACK
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private lateinit var toolbarBack: androidx.appcompat.widget.Toolbar
    private lateinit var coverImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var excerptDuration: TextView
    private lateinit var album: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var addButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var track: Track
    private val mediaPlayer = Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val setTimeRunnable = Runnable { setTime() }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_player)
        initViews()
        mediaPlayer.preparePlayer(track.previewUrl)
        mediaPlayer.setOnStateChangeListener { state ->
            playButton.setOnClickListener {
                playbackControl(state)
            }
        }
    }

    private fun initViews() {
        toolbarBack = findViewById(id.player_toolbars)
        toolbarBack.setNavigationOnClickListener { finish() }
        coverImage = findViewById(id.cover)
        trackName = findViewById(id.track_name)
        artistName = findViewById(id.artist_name)
        duration = findViewById(id.changeable_duration)
        album = findViewById(id.album)
        albumName = findViewById(id.changeable_album)
        year = findViewById(id.changeable_year)
        genre = findViewById(id.changeable_genre)
        country = findViewById(id.changeable_country)
        addButton = findViewById(id.add_button)
        playButton = findViewById(id.play_button)
        likeButton = findViewById(id.like_button)
        excerptDuration = findViewById(id.excerpt_duration)
        track = Gson().fromJson((intent.getStringExtra(TRACK)), Track::class.java)
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(drawable.ic_toast)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(dimen.cornerRadius_8)))
            .into(coverImage)
        trackName.text = track.trackName
        artistName.text = track.artistName
        if (track.trackTimeMillis?.isEmpty() == false) {
            duration.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis!!.toLong())
        } else duration.text = getString(string.time_null)
        albumName.text = track.collectionName
        track.releaseDate?.let { year.text = it.substring(0, 4) }
        genre.text = track.primaryGenreName
        country.text = track.country
        excerptDuration.text = getString(string.time_null)
        if (track.collectionName?.isEmpty() == false) {
            albumName.text = track.collectionName
        } else {
            album.visibility = View.GONE
        }
    }

    private fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                mediaPlayer.startPlayer()
                updatePlayButton(drawable.ic_pause_button)
                handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
            }
            PlayerState.STATE_PLAYING -> {
                mediaPlayer.pausePlayer()
                updatePlayButton(drawable.ic_play_button)
                handler.removeCallbacks(setTimeRunnable)
            }
        }
    }

    private fun updatePlayButton(imageResource: Int) {
        playButton.setImageResource(imageResource)
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer.startPlayer()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(setTimeRunnable)
    }

    private fun setTime() {
        excerptDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getPosition())
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    companion object {
        private const val SET_TIME_DELAY = 400L
    }

}