package com.example.playlistmaker.search.data.localStorage

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesClient(private val sharedPreferences: SharedPreferences): HistoryRepository {

   private fun saveHistory(historyList: List<Track>) {

        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

   override fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type) ?: ArrayList()
    }

   override fun clear() {
        saveHistory(ArrayList())
    }

   override fun write(track: Track) {
        val searchHistory = read()
        if (searchHistory.contains(track)) {
            searchHistory.remove(track)
            searchHistory.add(0, track)
        } else {
            if (searchHistory.size == 10) {
                searchHistory.removeAt(9)
                searchHistory.add(0, track)
            } else {
                searchHistory.add(0, track)
            }
        }
        saveHistory(searchHistory)
    }



    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
    }

}