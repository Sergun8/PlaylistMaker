package com.example.playlistmaker.DI

import com.example.playlistmaker.mediateca.ui.FavoriteViewModel
import com.example.playlistmaker.mediateca.ui.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MediatecaModule = module {

    viewModel {
        FavoriteViewModel()
    }
    viewModel {
        PlaylistViewModel()
    }
}
