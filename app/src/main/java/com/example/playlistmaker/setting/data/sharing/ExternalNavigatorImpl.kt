package com.example.playlistmaker.setting.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.setting.domain.sharing.EmailData
import com.example.playlistmaker.setting.domain.sharing.api.ExternalNavigator


class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {

    override fun shareLink(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        context.startActivity(intent)
    }

    override fun openLink(termsLink: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(termsLink)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.textMessage)
        context.startActivity(intent)
    }

    override fun sharePlaylist(sharePlaylist: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, sharePlaylist)
        }
        context.startActivity(intent)
    }
}
