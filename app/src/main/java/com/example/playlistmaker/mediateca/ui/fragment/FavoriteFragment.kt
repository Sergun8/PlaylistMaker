package com.example.playlistmaker.mediateca.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLikeTrackBinding
import com.example.playlistmaker.mediateca.ui.viewModel.FavoriteState
import com.example.playlistmaker.mediateca.ui.viewModel.FavoriteViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.search.SearchFragment
import com.example.playlistmaker.search.ui.search.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var adapter: TrackAdapter? = null
    private lateinit var rvFavoriteList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true

    private val favoriteTracksViewModel: FavoriteViewModel by viewModel()

    private lateinit var binding: FragmentLikeTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeTrackBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        favoriteTracksViewModel.fillData()
        favoriteTracksViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initView() {
        with(binding) {
            rvFavoriteList = favoriteList

            this@FavoriteFragment.progressBar = progressBar
        }

        adapter = TrackAdapter {
            if (clickDebounce()) {
                val trackJson = Gson().toJson(it)
                findNavController().navigate(
                    R.id.action_mediaFragment_to_playerFragment,
                    PlayerFragment.createArgs(trackJson)
                )
            }
        }
        rvFavoriteList.adapter = adapter
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Loading -> showLoading()
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showPlaceholder()

        }
    }

    private fun showEmptyScreen() {
        progressBar.isGone = true
        rvFavoriteList.isGone = true
        binding.placeholder.isGone = true
        binding.placeholderMessage.isGone = true
    }

    private fun showLoading() {
        showEmptyScreen()
        progressBar.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        showEmptyScreen()
        rvFavoriteList.isVisible = true
        adapter?.trackList?.clear()
        adapter?.trackList?.addAll(tracks)
        adapter?.notifyDataSetChanged()
    }

    private fun showPlaceholder() {
        showEmptyScreen()
        binding.placeholderMessage.text = getString(R.string.mediateca_empty)
        binding.placeholderMessage.isVisible = true
        binding.placeholder.setImageResource(R.drawable.ic_nothing_found)
        binding.placeholder.isVisible = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        rvFavoriteList.adapter = null
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.fillData()
        isClickAllowed = true
    }

    companion object {
        private const val PAGE = "PAGE"

        fun newInstance(number: Int) = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE, number)
            }
        }
    }
}