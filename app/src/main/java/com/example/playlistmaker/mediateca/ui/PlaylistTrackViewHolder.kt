package com.example.playlistmaker.mediateca.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateca.domain.Playlist

class PlaylistTrackViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val title: TextView = itemView.findViewById(R.id.playlist_name)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val picture: ImageView = itemView.findViewById(R.id.cover)

    fun bind(playlist: Playlist) {
        title.text = playlist.playlistName
        description.text = "${ playlist.amountTrack} треков"
        if(playlist.preview!=null)
            Glide.with(itemView)
                .load(playlist.preview)
                .placeholder(R.drawable.ic_toast)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(picture)
    }
}