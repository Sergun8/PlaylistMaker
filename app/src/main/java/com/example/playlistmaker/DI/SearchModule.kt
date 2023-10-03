package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.localStorage.HistoryRepository
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SearchModule = module {single<TrackRepository> {
    TrackRepositoryImpl(networkClient = get(), historyRepository = get())
}
    single<NetworkClient> {
        RetrofitNetworkClient(context = get())
    }
    single<HistoryRepository> {
        SharedPreferencesClient(sharedPreferences = get())
    }
    factory<SearchInteractor> {
        TrackInteractorImpl(repository = get())
    }
    single {
        val SHARED_PREFERENCE = "SHARED_PREFERENCE"
        androidContext().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }
    viewModel<SearchViewModel> {
        SearchViewModel(interactor = get())
    }
}