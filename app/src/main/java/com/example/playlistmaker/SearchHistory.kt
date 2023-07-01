package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.Player.domain.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun saveHistory(historyList: ArrayList<Track>) {

        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun readHistory(): Array<Track> {

        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)

    }
    companion object {
       private const val HISTORY_KEY = "HISTORY_KEY"
    }

}