package com.example.playlistmaker.mediateca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatecaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatecaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMediatecaBinding
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatecaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediatecaToolbars.setNavigationOnClickListener { finish() }

        binding.viewPager.adapter = TrackViewAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.like_tracks)
                }

                1 -> {
                    tab.text = getString(R.string.playlists)
                }
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}