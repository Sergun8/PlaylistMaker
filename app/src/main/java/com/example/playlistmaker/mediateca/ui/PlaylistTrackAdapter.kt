package com.example.playlistmaker.mediateca.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateca.domain.Playlist

class PlaylistTrackAdapter(): RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    private var playlists = ArrayList<Playlist>()

    private var itemClickListener: ((Int, Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, playlist) }
    }
}