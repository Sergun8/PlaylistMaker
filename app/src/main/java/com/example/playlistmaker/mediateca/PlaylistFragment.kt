package com.example.playlistmaker.mediateca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateca.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private val playlistsViewModel: PlaylistViewModel by viewModel {
        parametersOf()
    }

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            placeholderMessage.text = getString(R.string.playlist_empty)
            placeholder.setImageResource(R.drawable.ic_nothing_found)
            newPlayList.visibility = View.VISIBLE
            newPlayList.text = getString(R.string.playlist_new)
        }
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