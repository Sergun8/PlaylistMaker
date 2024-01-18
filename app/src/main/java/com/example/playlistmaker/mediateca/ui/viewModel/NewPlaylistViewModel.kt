package com.example.playlistmaker.mediateca.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun savePlaylist(name: String, description: String?, preview: String) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(createPlaylist(name, description, preview))
        }
    }
    fun updatePlaylist(playlistID: Long, name: String, description: String?, preview: String) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(updatePlaylist(name, description, preview, playlistID,))
        }
    }
    private fun updatePlaylist(name: String, description: String?, preview: String, playlistID: Long): Playlist {
        return Playlist(
            playlistName = name,
            description = description,
            preview = preview,
            playlistId = playlistID
        )
    }
    private fun createPlaylist(name: String, description: String?, preview: String): Playlist {
        return Playlist(
            playlistName = name,
            description = description,
            preview = preview,
            playlistId = null
        )
    }

}