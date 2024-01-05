package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.R.drawable
import com.example.playlistmaker.R.string
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerFragment : Fragment() {

    private lateinit var track: Track
    private lateinit var binding: FragmentPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()


    private var currentPlaylist: String? = ""

    private val playlistAdapterMedia = PlaylistBottomShadowViewAdapter {
        currentPlaylist = it.playlistName
        viewModel.addTrackInPlaylist(it, track)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        binding.playerToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.preparePlayer(track.previewUrl)

        viewModel.observePlayState().observe(viewLifecycleOwner) { state ->
            binding.playButton.setOnClickListener {
                playbackControl(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.excerptDuration.text = getString(string.time_null)
                viewModel.onPause()
                binding.playButton.setImageResource(drawable.ic_play_button)

            }
        }
        viewModel.observeDurationState().observe(viewLifecycleOwner) {
            binding.excerptDuration.text = it
        }
        viewModel.checkIsFavourite(track)

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }
        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            binding.likeButton.setImageResource(
                if (isFavorite) drawable.ic_like_color else drawable.ic_like
            )
        }
        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playlistsRecyclerView.adapter = playlistAdapterMedia

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaylist()
            viewModel.playlistLiveData().observe(viewLifecycleOwner) { list ->
                playlistAdapterMedia.setPlaylistItem(list)
                binding.playlistsRecyclerView.adapter = playlistAdapterMedia
            }

        }
        binding.newPlayList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.constraintLayout.visibility=View.GONE
            binding.fragmentContainer.visibility=View.VISIBLE
                findNavController()
                    .navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        viewModel.addTrackInPlaylistLiveData().observe(viewLifecycleOwner) {
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

    private fun initViews() {



        track = Gson().fromJson(requireArguments().getString(ARGS_TRACK), Track::class.java)

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
        val message = if (isInPlaylist) resources.getString(string.track_in_playlist)
        else resources.getString(string.added_playlist)
        Toast.makeText(requireContext(), "$message $currentPlaylist", Toast.LENGTH_LONG).show()
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

    companion object {
        const val ARGS_TRACK = "track"
        fun createArgs(track: String): Bundle =
            bundleOf(ARGS_TRACK to track)

    }
}