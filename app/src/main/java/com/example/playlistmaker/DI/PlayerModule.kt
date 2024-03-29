package com.example.playlistmaker.DI

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlayerModule = module {
    factory {
        MediaPlayer()
    }
    factory <PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(repository = get(), favoriteTrackRepository = get(), playlistRepository = get())
    }
    viewModel {
        PlayerViewModel(
            mediaPlayerInteractor = get(), playlistInteractor = get()
        )
    }
}