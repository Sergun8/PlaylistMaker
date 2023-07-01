package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Player.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvTrackName: TextView = itemView.findViewById(R.id.track_name)
    private val tvArtistName: TextView = itemView.findViewById(R.id.artist_name)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.track_time)
    private val ivArtworkUrl100: ImageView = itemView.findViewById(R.id.artwork_url_100)


    fun bind(item: Track) {

        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text  = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_toast)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.cornerRadius_2)))
            .into(ivArtworkUrl100)
    }
}