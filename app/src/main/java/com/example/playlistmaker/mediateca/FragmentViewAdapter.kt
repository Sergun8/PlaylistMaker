package com.example.playlistmaker.mediateca

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.mediateca.ui.fragment.FavoriteFragment
import com.example.playlistmaker.mediateca.ui.fragment.PlaylistFragment

class FragmentViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            FavoriteFragment.newInstance(position + 1)
        else
            PlaylistFragment.newInstance(position+1)
    }
}