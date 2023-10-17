package com.example.playlistmaker.mediateca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLikeTrackBinding
import com.example.playlistmaker.mediateca.ui.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment: Fragment() {

    companion object {
        private const val PAGE = "PAGE"

        fun newInstance(number: Int) = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE, number)
            }
        }
    }

    private val favoriteTracksViewModel: FavoriteViewModel by viewModel {
        parametersOf()
    }

    private lateinit var binding: FragmentLikeTrackBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLikeTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeholderMessage.text = getString(R.string.mediateca_empty)
        binding.placeholder.setImageResource(R.drawable.ic_nothing_found)
    }
}