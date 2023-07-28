package com.example.playlistmaker.setting.domain.impl

import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}