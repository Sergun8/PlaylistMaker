package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchClick = findViewById<Button>(R.id.button_search)
        val mediatecaClick = findViewById<Button>(R.id.button_mediateca)
        val settingClick = findViewById<Button>(R.id.button_settings)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        mediatecaClick.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MediatecaActivity::class.java)
            startActivity(displayIntent)
        }
        searchClick.setOnClickListener(searchClickListener)
        settingClick.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_settings -> {
                val displayIntent = Intent(this, SettingActivity::class.java)
                startActivity(displayIntent)
            }
        }

    }

}