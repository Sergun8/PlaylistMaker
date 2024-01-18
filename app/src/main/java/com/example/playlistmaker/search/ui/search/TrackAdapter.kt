package com.example.playlistmaker.search.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(private val clickListener: TrackClickListener)

    : RecyclerView.Adapter<TrackViewHolder>() {
    var trackList = ArrayList<Track>()
    var onLongTrackClick: ((Track) -> Boolean)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackList[position]) }
        holder.itemView.setOnLongClickListener { onLongTrackClick?.invoke(trackList[position]) == true }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}