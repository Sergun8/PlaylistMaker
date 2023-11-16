package com.example.playlistmaker.setting.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingBinding
import com.example.playlistmaker.setting.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModel<SettingsViewModel>()

        val themeSwitchDark = binding.switchDarck
        val clickAgreement = binding.buttomAgreement
        val clickHelp = binding.buttomHelp
        val clickShare = binding.buttomShare


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

