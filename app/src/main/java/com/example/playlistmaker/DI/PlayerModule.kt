package com.example.playlistmaker.DI

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlayerModule = module{
    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }
    viewModel<PlayerViewModel> {
        PlayerViewModel(
            mediaPlayerInteractor =  get()
        )
    }
}