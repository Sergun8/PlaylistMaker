package com.example.playlistmaker.DI

import androidx.room.Room
import com.example.playlistmaker.mediateca.data.AppDatabase
import com.example.playlistmaker.mediateca.data.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.mediateca.data.PlaylistRepositoryImpl
import com.example.playlistmaker.mediateca.data.db.convertors.PlaylistDbConvertor
import com.example.playlistmaker.mediateca.data.db.convertors.TrackDbConvertor
import com.example.playlistmaker.mediateca.data.db.convertors.TrackInPlaylistDbConvertor
import com.example.playlistmaker.mediateca.domain.FavoriteTrackRepository
import com.example.playlistmaker.mediateca.domain.FavoriteInteractor
import com.example.playlistmaker.mediateca.domain.FavoriteInteractorImpl
import com.example.playlistmaker.mediateca.domain.PlaylistInteractor
import com.example.playlistmaker.mediateca.domain.PlaylistInteractorImpl
import com.example.playlistmaker.mediateca.domain.PlaylistRepository
import com.example.playlistmaker.mediateca.ui.viewModel.FavoriteViewModel
import com.example.playlistmaker.mediateca.ui.viewModel.NewPlaylistViewModel
import com.example.playlistmaker.mediateca.ui.viewModel.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MediatecaModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }
    factory { TrackDbConvertor() }
    factory { PlaylistDbConvertor(get()) }
    factory { TrackInPlaylistDbConvertor() }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        FavoriteViewModel(favoriteInteractor = get())
    }
    viewModel {
        PlaylistViewModel(playlistInteractor = get())
    }
    viewModel {
        NewPlaylistViewModel(playlistInteractor = get())
    }
}
