package com.example.playlistmaker.search.data

import com.example.playlistmaker.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.localStorage.HistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.ErrorNetwork


class TrackRepositoryImpl(
    private val networkClient: NetworkClient, private val historyRepository: HistoryRepository
) : TrackRepository {

    override fun search (expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE)
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.country,
                        it.primaryGenreName,
                        it.releaseDate,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE)
            }

        }
    }

    override fun readHistory(): ArrayList<Track> {
        return historyRepository.read()
    }

    override fun clearHistory() {
        historyRepository.clear()
    }

    override fun addTrackHistory(track: Track) {
        historyRepository.write(track)
    }
}

