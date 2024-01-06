package com.example.playlistmaker.mediateca.data.db.convertors

import com.example.playlistmaker.mediateca.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackInPlaylistDbConvertor {
    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId.toLong(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl
        )
    }

    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.trackId.toString(),
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
