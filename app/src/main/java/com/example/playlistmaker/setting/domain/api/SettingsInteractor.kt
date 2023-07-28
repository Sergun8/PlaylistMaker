package com.example.playlistmaker.setting.domain.api

import com.example.playlistmaker.setting.domain.ThemeSettings


interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}
