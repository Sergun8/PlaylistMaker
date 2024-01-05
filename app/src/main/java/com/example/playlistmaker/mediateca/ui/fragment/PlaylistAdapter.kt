package com.example.playlistmaker.mediateca.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateca.domain.Playlist

class PlaylistAdapter(): RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist, parent, false)
        return PlaylistViewHolder(view)
    }
    override fun getItemCount(): Int {
        return playlists.size
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}