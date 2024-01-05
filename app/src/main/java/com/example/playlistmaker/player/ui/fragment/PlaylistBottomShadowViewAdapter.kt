package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateca.domain.Playlist

class PlaylistBottomShadowViewAdapter (private var onClickPlaylistItem: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistBottomShadowViewHolder>() {

    private val playlistItems = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomShadowViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet, parent, false)
        return PlaylistBottomShadowViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistBottomShadowViewHolder, position: Int) {
        holder.bind(playlistItems[position])
        holder.itemView.setOnClickListener {
            onClickPlaylistItem.invoke(playlistItems[position])
        }
    }

    override fun getItemCount(): Int {
        return playlistItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPlaylistItem(newPlaylistItems: List<Playlist>?) {
        playlistItems.clear()
        if (!newPlaylistItems.isNullOrEmpty()) {
            playlistItems.addAll(newPlaylistItems)
        }
        notifyDataSetChanged()
    }
}