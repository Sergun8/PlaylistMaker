package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.localStorage.HistoryRepository
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.data.network.ItunesAPI
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val SearchModules = module {
    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }
    factory {
        Gson()
    }
    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get(), historyRepository = get())
    }
    single<NetworkClient> {
        RetrofitNetworkClient(context = get(), itunesService = get())
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
    viewModel {
        SearchViewModel(interactor = get())
    }
}