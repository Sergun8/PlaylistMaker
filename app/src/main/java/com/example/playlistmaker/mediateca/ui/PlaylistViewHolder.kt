package com.example.playlistmaker.mediateca.ui

import android.annotation.SuppressLint
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.ui.fragment.NewPlaylistFragment
import java.io.File


class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val amountTracks: TextView = itemView.findViewById(R.id.amount_tracks)
    private val image: ImageView = itemView.findViewById(R.id.playlist_cover)

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    fun bind(playlist: Playlist) {
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NewPlaylistFragment.PICTURES
        )
        playlistName.text = playlist.playlistName
        amountTracks.text = "${playlist.amountTrack} ${
            itemView.resources.getQuantityString(
                R.plurals.track_amount,
                playlist.amountTrack.toInt()
            )
        }"
        Glide.with(itemView)
            .load( File(filePath, playlist.preview!!))
            .placeholder(R.drawable.ic_toast)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(dimen.cornerRadius_8))
            )
            .into(image)
    }
}