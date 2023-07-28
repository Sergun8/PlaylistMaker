package com.example.playlistmaker


import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.localStorage.HistoryRepository
import com.example.playlistmaker.search.data.localStorage.SharedPreferencesClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.search.ui.search.SearchActivity.Companion.SHARED_PREFS
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl.Companion.DARK_THEME_ENABLED
import com.example.playlistmaker.setting.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.sharing.api.ExternalNavigator
import com.example.playlistmaker.setting.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.setting.domain.sharing.impl.SharingInteractorImpl


object Creator {

    private const val SHARED_PREFERENCE = "SHARED_PREFERENCE"
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return TrackInteractorImpl(getSearchRepository(context))
    }

    private fun getSearchRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(
            getNetworkClient(context),
            getSearchHistory(context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE))
        )
    }
    private fun getSearchHistory(sharedPreferences: SharedPreferences): HistoryRepository {
        return SharedPreferencesClient(sharedPreferences)
    }

    private fun getNetworkClient(context: Context): RetrofitNetworkClient {
        return RetrofitNetworkClient(context)
    }
    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context.getSharedPreferences(DARK_THEME_ENABLED, Context.MODE_PRIVATE)))
    }

    private fun provideSettingsRepository(sharedPreferences: SharedPreferences) : SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences)
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(context))
    }

    private fun provideExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

}
