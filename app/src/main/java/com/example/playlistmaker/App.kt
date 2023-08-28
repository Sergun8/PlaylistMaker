package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator.provideSettingsInteractor
import com.example.playlistmaker.setting.domain.ThemeSettings


class App : Application() {

    override fun onCreate() {


        val settingsInteractor = provideSettingsInteractor(applicationContext)
        darkMode(settingsInteractor.getThemeSettings())

        super.onCreate()
    }

    private fun darkMode(isDarkMode: ThemeSettings) {
        if (isDarkMode.darkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}



