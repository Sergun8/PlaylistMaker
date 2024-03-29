package com.example.playlistmaker.mediateca.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.ui.viewModel.PlaylistState
import com.example.playlistmaker.mediateca.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var adapter: PlaylistAdapter? = null
    private val playlistsViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        binding.newPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_mediatecaFragment_to_newPlaylistFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.fillData()
        adapter = PlaylistAdapter {
            findNavController().navigate(
                R.id.action_mediatecaFragment_to_infoPlaylistFragment,
                InfoPlaylistFragment.createArgs(it.playlistId!!)
            )
        }

        with(binding) {
            placeholderMessage.text = getString(R.string.playlist_empty)
            placeholder.setImageResource(R.drawable.ic_nothing_found)
            newPlayList.visibility = View.VISIBLE
            newPlayList.text = getString(R.string.playlist_new)
            rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
            rvPlaylists.adapter = adapter
        }



        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.PlaylistContent -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Loading -> showLoading()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showLoading() {
        with(binding) {
            placeholder.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            rvPlaylists.visibility = View.GONE
            newPlayList.visibility = View.VISIBLE
        }
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showEmpty() {
        with(binding) {
            newPlayList.visibility = View.VISIBLE
            placeholder.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE
            rvPlaylists.visibility = View.GONE
        }
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(playlist: List<Playlist>) {
        with(binding) {
            newPlayList.visibility = View.VISIBLE
            placeholder.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            rvPlaylists.visibility = View.VISIBLE
        }
        adapter?.playlists?.clear()
        adapter?.playlists?.addAll(playlist)
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.fillData()
    }

    override fun onStop() {
        super.onStop()
        playlistsViewModel.onCleared()
    }

    override fun onStart() {
        super.onStart()
        playlistsViewModel.fillData()
    }

    companion object {
        private const val PAGE = "PAGE"
        fun newInstance(number: Int) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE, number)
            }
        }
    }

}



