package com.example.playlistmaker.search.data

import com.example.playlistmaker.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.localStorage.HistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.ErrorNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackRepositoryImpl(
    private val networkClient: NetworkClient, private val historyRepository: HistoryRepository
) : TrackRepository {

    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(dto = TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ErrorNetwork.NO_CONNECTIVITY_MESSAGE))
            }

            200 -> {
                emit(Resource.Success((response as TrackSearchResponse).results.map {
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
                }))
            }

            else -> {
                emit(Resource.Error(ErrorNetwork.SERVER_ERROR_MESSAGE))
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

