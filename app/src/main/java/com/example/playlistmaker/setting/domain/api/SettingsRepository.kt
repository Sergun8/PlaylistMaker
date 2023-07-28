package com.example.playlistmaker.setting.domain.api

import com.example.playlistmaker.setting.domain.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}