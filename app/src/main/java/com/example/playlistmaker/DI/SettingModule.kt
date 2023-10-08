package com.example.playlistmaker.DI

import android.content.Context
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import com.example.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.setting.domain.sharing.api.ExternalNavigator
import com.example.playlistmaker.setting.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.setting.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SettingModule  = module{
    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPreferences = get())
    }
    factory <SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get() )
    }
    factory <SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }
    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }
    single {
        androidContext().getSharedPreferences(SettingsRepositoryImpl.DARK_THEME_ENABLED, Context.MODE_PRIVATE)
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }
}