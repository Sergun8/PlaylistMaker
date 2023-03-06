package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView


class SettingActivity : AppCompatActivity(), View.OnClickListener {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val back = findViewById<ImageView>(R.id.back_setting)
        back.setOnClickListener(this@SettingActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_setting -> {
                finish()
            }

        }
    }
}