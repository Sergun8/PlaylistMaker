package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchInteractor.TrackConsumer) {
        executor.execute {

            when (val resource = repository.search(expression)) {

                is Resource.Success -> {
                    consumer.consume(resource.data , resource.message)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun readHistory(): ArrayList<Track> {
        return repository.readHistory()
    }

    /*  override fun saveHistory(track: Track) {
          repository.SaveHistory(track)
      }
  */
    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addTrackHistory(track: Track) {
        repository.addTrackHistory(track)
    }

}