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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.textMessage)
        context.startActivity(intent)
    }
}
/*
class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(shareAppLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(termsLink)).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.textMessage)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }
}
*/