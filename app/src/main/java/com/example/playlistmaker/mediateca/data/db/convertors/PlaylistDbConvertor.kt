package com.example.playlistmaker.mediateca.data.db.convertors

import com.example.playlistmaker.mediateca.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateca.domain.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PlaylistDbConvertor(private var json: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.description,
            playlist.preview,
            playlist.amountTrack,
            trackIds = json.toJson(playlist.trackIds).toString()
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.description,
            playlist.preview?: "",
            playlist.amountTrack,
            trackIds = createTrackListFromJson(playlist.trackIds)
        )
    }

    private fun createTrackListFromJson(jsonTrackList: String?): MutableList<Long> {
        var trackIds = mutableListOf<Long>()
        if (!jsonTrackList.isNullOrEmpty()) {
            trackIds = json.fromJson(jsonTrackList, object : TypeToken<List<Long>>() {}.type)
        }
        return trackIds
    }
}
