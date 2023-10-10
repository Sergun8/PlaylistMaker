package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.R.drawable
import com.example.playlistmaker.R.string
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.ui.search.SearchActivity.Companion.TRACK
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        viewModel.preparePlayer(track.previewUrl)

        viewModel.observePlayState().observe(this) { state ->
            binding.playButton.setOnClickListener {
                playbackControl(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.excerptDuration.text = getString(string.time_null)
                viewModel.onPause()
                binding.playButton.setImageResource(drawable.ic_play_button)

            }
        }
        viewModel.observeDurationState().observe(this) {
            binding.excerptDuration.text = it
        }

    }

    private fun initViews() {

        binding.playerToolbars.setNavigationOnClickListener {
            finish()
        }
        track = Gson().fromJson((intent.getStringExtra(TRACK)), Track::class.java) as Track
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(drawable.ic_toast)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(dimen.cornerRadius_8)))
            .into(binding.cover)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        if (track.trackTimeMillis?.isEmpty() == false) {
            binding.changeableDuration.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis!!.toLong())
        } else binding.changeableDuration.text = getString(string.time_null)
        binding.changeableAlbum.text = track.collectionName
        track.releaseDate.let { binding.changeableYear.text = it.substring(0, 4) }
        binding.changeableGenre.text = track.primaryGenreName
        binding.changeableCountry.text = track.country
        binding.excerptDuration.text = getString(string.time_null)
        if (track.collectionName?.isEmpty() == false) {
            binding.changeableAlbum.text = track.collectionName
        } else {
            binding.changeableAlbum.visibility = View.GONE
        }
    }

    private fun playbackControl(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_COMPLETE -> {
                binding.playButton.setImageResource(drawable.ic_pause_button)
                viewModel.onStart()
            }

            PlayerState.STATE_PLAYING -> {
                binding.playButton.setImageResource(drawable.ic_play_button)
                viewModel.onPause()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


}