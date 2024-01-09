package com.example.playlistmaker.mediateca.ui.fragment

import android.annotation.SuppressLint

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentInfoPlaylistBinding
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.ui.viewModel.InfoPlaylistState
import com.example.playlistmaker.mediateca.ui.viewModel.InfoPlaylistViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.search.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class InfoPlaylistFragment : Fragment() {

    private var _binding: FragmentInfoPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoPlaylistViewModel by viewModel()
    private lateinit var currentPlaylist: Playlist
    private lateinit var tracks: List<Track>
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPlaylistBinding.inflate(inflater, container, false)
        binding.infoPlaylistToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //
        viewModel.getInfoPlaylist(requireArguments().getLong("ARGS_PLAYLIST"))
        adapter = TrackAdapter {

            findNavController().navigate(
                R.id.action_infoPlaylistFragment_to_playerFragment,
                PlayerFragment.createArgs(Gson().toJson(it))
            )
        }

        adapter.onLongTrackClick = { track ->
            deleteTrackDialog(track.trackId, currentPlaylist.playlistId!!)
            true
        }
        binding.recyclerViewTrack.adapter = adapter

        viewModel.stateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.ibShare.setOnClickListener {
            viewModel.sharePlaylist(tracks, currentPlaylist)
        }

        val bottomSheetContainer = binding.bottomSheetMenu
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        val bottomSheetBehaviorTrack =
            BottomSheetBehavior.from(binding.playlistTracksBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        binding.ibMore.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showPlaylistMenuMore()
        }
        binding.shareBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist(tracks, currentPlaylist)
        }
        binding.deleteBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            delPlaylistDialog(currentPlaylist)
        }
        binding.editBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            editPlaylist(currentPlaylist)
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        bottomSheetBehaviorTrack.state = BottomSheetBehavior.STATE_COLLAPSED
                        bottomSheetBehaviorTrack.isHideable = false
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        bottomSheetBehaviorTrack.isHideable = true
                        bottomSheetBehaviorTrack.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun render(state: InfoPlaylistState) {
        when (state) {
            is InfoPlaylistState.Content -> showPlaylist(state.playlist, state.tracks)
            is InfoPlaylistState.Empty -> shareDialog()

        }
    }

    private fun editPlaylist(editPlaylist: Playlist) {

        findNavController()
            .navigate(
                R.id.action_infoPlaylistFragment_to_NewPlaylistFragment,
                NewPlaylistFragment.createArgs(
                    editPlaylist.playlistId,
                    editPlaylist.playlistName,
                    editPlaylist.description,
                    editPlaylist.preview
                )
            )

    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun showPlaylist(playlist: Playlist, track: List<Track>) {
        currentPlaylist = playlist
        tracks = track
        adapter.trackList.clear()
        adapter.trackList.addAll(track)
        adapter.notifyDataSetChanged()
        binding.apply {
            recyclerViewTrack.adapter = adapter
            tvName.text = playlist.playlistName
            tvDescipt.text = playlist.description
            tvPlaylistCount.text = "${playlist.amountTrack} ${
                root.context.resources.getQuantityString(
                    R.plurals.track_amount,
                    playlist.amountTrack.toInt()
                )
            }"
            val filePath = File(
                root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                NewPlaylistFragment.PICTURES
            )
            tvPlaylistTime.text = "${timePlaylist(tracks = track)} ${
                root.context.resources.getQuantityString(
                    R.plurals.time_amount,
                    timePlaylist(tracks = track).toInt()
                )
            }"
            Glide.with(root.context)
                .load(File(filePath, playlist.preview!!))
                .transform(CenterCrop())
                .placeholder(R.drawable.ic_toast)
                .into(ivCoverPlaylist)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showPlaylistMenuMore() {
        binding.apply {
            val filePath = File(
                root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                NewPlaylistFragment.PICTURES
            )
            nameBottomSheet.text = currentPlaylist.playlistName
            tracksBottomSheet.text = "${currentPlaylist.amountTrack} ${
                root.context.resources.getQuantityString(
                    R.plurals.track_amount,
                    currentPlaylist.amountTrack.toInt()
                )
            }"
            Glide.with(requireContext())
                .load(File(filePath, currentPlaylist.preview!!))
                .transform(RoundedCorners(8))
                .centerCrop()
                .placeholder(R.drawable.ic_toast)
                .into(binding.ivBottomSheet)
        }

    }

    private fun timePlaylist(tracks: List<Track>): String {
        var result = 0L
        for (track in tracks) {
            result += track.trackTimeMillis!!.toLong()
        }
        return SimpleDateFormat("mm", Locale.getDefault()).format(result)
    }

    private fun deleteTrackDialog(trackId: String, playlistId: Long) {
              MaterialAlertDialogBuilder(requireActivity(),R.style.AlertTheme)
            .setTitle("Удалить трек?")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена")
            { _, _ -> }
            .setPositiveButton("Удалить")
            { _, _ ->
                deleteTrackFromPlaylist(trackId, playlistId)
            }
            .show()
    }

    private fun delPlaylistDialog(playlist: Playlist) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertTheme)
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNeutralButton("Нет") { _, _ -> }
            .setPositiveButton("Да") { _, _ ->
                delPlaylist(playlist.playlistId!!)
                delFilePicture(playlist.preview!!)
            }
            .show()
    }

    private fun shareDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("В этом плейлисте нет списка треков, которым можно поделиться")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun deleteTrackFromPlaylist(trackId: String, playlistId: Long) {
        viewModel.deleteTrackFromPlaylist(trackId, playlistId)
    }

    private fun delPlaylist(playlistId: Long) {
        viewModel.delPlaylist(playlistId)
        findNavController().navigateUp()
    }
    private fun delFilePicture(preview: String) {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NewPlaylistFragment.PICTURES
        )
        File(filePath, "$preview.ipg").delete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun createArgs(playlistId: Long): Bundle =
            bundleOf("ARGS_PLAYLIST" to playlistId)

    }
}