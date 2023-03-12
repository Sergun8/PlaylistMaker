package com.example.playlistmaker


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbars)
        toolbar.setNavigationOnClickListener { finish() }
    }

}