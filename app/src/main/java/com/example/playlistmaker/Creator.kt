package com.example.playlistmaker


import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl



object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }


    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(),
            SharedPreferencesClient(SharedPreferencesClient.HISTORY_KEY)
        )
    }
    fun provideSearchInteractor(): SearchInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }



  }