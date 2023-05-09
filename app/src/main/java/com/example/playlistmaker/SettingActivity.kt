package com.example.playlistmaker


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.playlistmaker.SearchActivity.Companion.NIGHT_THEME
import com.example.playlistmaker.SearchActivity.Companion.SHARED_PREFS
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val themeSwitchDark = findViewById<SwitchMaterial>(R.id.switch_darck)
        val clickAgreement = findViewById<LinearLayout>(R.id.buttom_agreement)
        val clickHelp = findViewById<LinearLayout>(R.id.buttom_help)
        val clickShare = findViewById<LinearLayout>(R.id.buttom_share)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbars)
        toolbar.setNavigationOnClickListener { finish() }
        clickAgreement.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))

            startActivity(browserIntent)
        }
        themeSwitchDark.isChecked = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            .getBoolean(NIGHT_THEME, false)

        themeSwitchDark.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            (applicationContext as App).saveTheme(checked)

        }

        clickHelp.setOnClickListener {
            val sendHelpIntent = Intent(Intent.ACTION_SENDTO)
            sendHelpIntent.data = Uri.parse("mailto:")
            sendHelpIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            sendHelpIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_theme))
            sendHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_massage))

            startActivity(sendHelpIntent)

        }
        clickShare.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            shareIntent.type = "text/plain"

            val shareApp = Intent.createChooser(shareIntent, null)
            startActivity(shareApp)
        }

    }

}

