package com.example.playlistmaker.search.data.localStorage

import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesClient(private val sharedPreferences: String) {

   private fun saveHistory(historyList: ArrayList<Track>) {

        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun readHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type) ?: ArrayList()
    }

    fun clearHistory() {
        saveHistory(ArrayList())
    }

    fun addTrackHistory(track: Track) {
        val searchHistory = readHistory()
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