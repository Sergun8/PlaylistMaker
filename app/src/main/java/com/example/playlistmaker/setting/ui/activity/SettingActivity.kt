package com.example.playlistmaker.setting.ui.activity



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbars)
        toolbar.setNavigationOnClickListener { finish() }

        val viewModel = ViewModelProvider(
            this,
            SettingsViewModel
                .getViewModelFactory(
                    Creator.provideSharingInteractor(application.applicationContext),
                    Creator.provideSettingsInteractor(application.applicationContext)
                )
        )[SettingsViewModel::class.java]

        val themeSwitchDark = findViewById<SwitchMaterial>(R.id.switch_darck)
        val clickAgreement = findViewById<LinearLayout>(R.id.buttom_agreement)
        val clickHelp = findViewById<LinearLayout>(R.id.buttom_help)
        val clickShare = findViewById<LinearLayout>(R.id.buttom_share)


        themeSwitchDark.isChecked = viewModel.observeDarkTheme().value == true

        themeSwitchDark.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        clickAgreement.setOnClickListener {
            viewModel.onServiceClick()
        }
        clickHelp.setOnClickListener {
            viewModel.onTermsClick()
        }

        clickShare.setOnClickListener {
            viewModel.onShareAppClick()
        }
    }
}

