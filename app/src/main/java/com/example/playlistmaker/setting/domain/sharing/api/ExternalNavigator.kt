package com.example.playlistmaker.setting.domain.sharing.api

import com.example.playlistmaker.setting.domain.sharing.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}