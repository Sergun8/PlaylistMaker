package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.SearchActivity.Companion.TRACK
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class PlayerActivity : AppCompatActivity() {

    private lateinit var toolbarBack: androidx.appcompat.widget.Toolbar
    private lateinit var coverImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var excerptDuration: TextView
    private lateinit var album: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var addButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var track: Track


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initViews()
    }

    private fun initViews() {
        toolbarBack = findViewById(R.id.player_toolbars)
        toolbarBack.setNavigationOnClickListener { finish() }
        coverImage = findViewById(R.id.cover)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        duration = findViewById(R.id.changeable_duration)
        album = findViewById(R.id.album)
        albumName = findViewById(R.id.changeable_album)
        year = findViewById(R.id.changeable_year)
        genre = findViewById(R.id.changeable_genre)
        country = findViewById(R.id.changeable_country)
        addButton = findViewById(R.id.add_button)
        playButton = findViewById(R.id.play_button)
        likeButton = findViewById(R.id.like_button)
        excerptDuration = findViewById(R.id.excerpt_duration)
        track = Gson().fromJson((intent.getStringExtra(TRACK)), Track::class.java)

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(R.drawable.ic_toast)
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(dimen.cornerRadius_8)))
            .into(coverImage)

        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text =
            formatTime(track)
        albumName.text = track.collectionName
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country
        excerptDuration.text =
            formatTime(track)
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text =
            formatTime(track)

        if (track.collectionName.isNotEmpty()) {
            albumName.text = track.collectionName
        } else {
            album.visibility = View.GONE
        }
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country
    }

    private fun formatTime(track: Track): CharSequence? {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
    }
}