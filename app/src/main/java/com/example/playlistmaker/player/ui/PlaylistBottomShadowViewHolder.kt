package com.example.playlistmaker.player.ui

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.ui.fragment.PlaylistFragment
import java.io.File

class PlaylistBottomShadowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val image: ImageView = itemView.findViewById(R.id.playlist_cover)
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val amountTrack: TextView = itemView.findViewById(R.id.amount_tracks)

    fun bind(playList: Playlist) {
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "mayAlbum"
        )


        Glide
            .with(itemView)
            .load(playList.preview)
            .placeholder(R.drawable.ic_toast)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.cornerRadius_2)))
            .into(image)
        name.text = playList.playlistName
        amountTrack.text = playList.amountTrack?.let {
            itemView.resources.getQuantityString(
                R.plurals.track_amount,
                it.toInt(), playList.amountTrack
            )
        }
    }
}

