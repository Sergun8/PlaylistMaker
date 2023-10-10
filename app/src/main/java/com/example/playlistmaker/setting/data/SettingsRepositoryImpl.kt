package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.setting.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return (ThemeSettings(sharedPreferences.getBoolean(DARK_THEME_ENABLED, false)))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_ENABLED, settings.darkThemeEnabled)
            .apply()
        setAppDarkTheme(settings.darkThemeEnabled)
    }

    private fun setAppDarkTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
    }
}

