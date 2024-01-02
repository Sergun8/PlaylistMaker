package com.example.playlistmaker.mediateca.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun savePlaylist(name: String, description: String?, preview: Uri?) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(createPlaylist(name, description, preview))
        }
    }

    private fun createPlaylist(name: String, description: String?, preview: Uri?): Playlist {
        return Playlist(
            playlistName = name,
            description = description,
            preview = preview.toString(),
            playlistId = null
        )
    }

}