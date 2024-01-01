package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.R.drawable
import com.example.playlistmaker.R.string
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.mediateca.ui.fragment.NewPlaylistFragment
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.ui.search.SearchFragment.Companion.TRACK
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()


    private var currentPlaylist: String? = ""

    private val playlistAdapterMedia = PlaylistBottomShadowViewAdapter {
        currentPlaylist = it.playlistName
        viewModel.addTrackInPlaylist(it, track)
    }


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
        viewModel.checkIsFavourite(track)

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }
        viewModel.observeIsFavorite().observe(this) { isFavorite ->
            binding.likeButton.setImageResource(
                if (isFavorite) drawable.ic_like_color else drawable.ic_like
            )
        }
        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaylist()
            viewModel.playlistLiveData().observe(this) { list ->
                playlistAdapterMedia.setPlaylistItem(list)
                binding.playlistsRecyclerView.adapter = playlistAdapterMedia
            }

        }





        binding.newPlayList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.constraintLayout.visibility=View.GONE
            binding.fragmentContainer.visibility=View.VISIBLE
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, NewPlaylistFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        viewModel.addTrackInPlaylistLiveData().observe(this) {
            if (!it) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showToast(it)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN ->     binding.overlayPlaylist.visibility = View.GONE
                    else -> binding.overlayPlaylist.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


    }
/*
        val adapter = PlaylistTrackAdapter()
        binding.playlistsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.playlistsRecyclerView.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            recyclerView.visibility = View.VISIBLE
            adapter.playlists.clear()
            adapter.playlists.addAll(it)
            adapter.notifyDataSetChanged()
        }

        adapter.itemClickListener = { _, playlist ->
            viewModel.onPlaylistClicked(playlist)
            adapter.notifyDataSetChanged()
        }
    }



 */

    private fun initViews() {

        binding.playerToolbars.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
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
    private fun showToast(isInPlaylist: Boolean) {
        val message = if (isInPlaylist) resources.getString(string.add_playlist)
        else resources.getString(string.add_playlist)
        Toast.makeText(applicationContext, "$message $currentPlaylist", Toast.LENGTH_LONG).show()
    }
    override fun onStart() {
        super.onStart()
        playbackControl(PlayerState.STATE_PLAYING)
    }

    override fun onPause() {
        super.onPause()
        playbackControl(PlayerState.STATE_PLAYING)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


}