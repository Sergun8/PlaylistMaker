package com.example.playlistmaker.setting.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.setting.domain.ThemeSettings
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.example.playlistmaker.setting.domain.sharing.api.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {


    private val darkThemeLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().darkThemeEnabled)

    fun observeDarkTheme(): LiveData<Boolean> = darkThemeLiveData

    fun onShareAppClick() {
        sharingInteractor.shareApp()
    }

    fun onServiceClick() {
        sharingInteractor.openTerms()
    }

    fun onTermsClick() {
        sharingInteractor.openSupport()
    }

    fun switchTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            settingsInteractor.updateThemeSetting(ThemeSettings(true))
            darkThemeLiveData.postValue(true)

        } else {
            settingsInteractor.updateThemeSetting(ThemeSettings(false))
            darkThemeLiveData.postValue(false)

        }
    }

    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        sharingInteractor,
                        settingsInteractor
                    ) as T
                }
            }
    }
}

