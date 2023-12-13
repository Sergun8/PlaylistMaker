package com.example.playlistmaker.mediateca.data.db.convertors

import com.example.playlistmaker.mediateca.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
            isFavorite = false,
            date = 0
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
    )
    }
}