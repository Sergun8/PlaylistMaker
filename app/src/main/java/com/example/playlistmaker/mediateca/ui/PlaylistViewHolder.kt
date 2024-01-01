package com.example.playlistmaker.mediateca.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateca.domain.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val amountOfTracks: TextView = itemView.findViewById(R.id.number_of_tracks)
    private val image: ImageView = itemView.findViewById(R.id.playlist_cover)
    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        playlistName.text = playlist.playlistName
        amountOfTracks.text = "${ playlist.amountTrack } треков"
        if(playlist.preview!=null)
            Glide.with(itemView)
                .load(playlist.preview)
                .placeholder(R.drawable.ic_toast)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(image)
    }
}