package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DI.PlayerModule
import com.example.playlistmaker.DI.SearchModule
import com.example.playlistmaker.DI.SettingModule
import com.example.playlistmaker.setting.domain.api.SettingsRepository
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private var darkTheme = false
    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@App)
            modules(
                SettingModule,
                SearchModule,
                PlayerModule
            )
        }
        switchTheme(getKoin().get<SettingsRepository>().getThemeSettings().darkThemeEnabled)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

