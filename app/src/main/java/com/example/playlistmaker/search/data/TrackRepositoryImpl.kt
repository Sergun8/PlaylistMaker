package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.utils.Resource


class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferencesClient: SharedPreferencesClient
) : TrackRepository {

    override fun search (expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            NO_CONNECTIVITY_ERROR -> {
                Resource.Error(NO_CONNECTIVITY_MESSAGE)
            }

            SUCCESSFUL_SEARCH_CODE -> {
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
                Resource.Error(SERVER_ERROR_MESSAGE)
            }
        }


    }

    override fun readHistory(): ArrayList<Track> {
        return sharedPreferencesClient.readHistory()
    }

    override fun clearHistory() {
        sharedPreferencesClient.clearHistory()
    }


    override fun addTrackHistory(track: Track) {
        sharedPreferencesClient.addTrackHistory(track)
    }


    companion object {
        const val SUCCESSFUL_SEARCH_CODE = 200
        const val NO_CONNECTIVITY_ERROR = -1
        const val NO_CONNECTIVITY_MESSAGE =
            "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"
        const val SERVER_ERROR_MESSAGE = "Ошибка сервера"
    }
}

