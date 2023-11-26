package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.ErrorNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : SearchInteractor {
    override fun search(expression: String): Flow<Pair<List<Track>?, ErrorNetwork?>> {

        return repository.search(expression).map { result ->
            when (result) {

                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun readHistory(): ArrayList<Track> {
        return repository.readHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addTrackHistory(track: Track) {
        repository.addTrackHistory(track)
    }

}