package com.example.playlistmaker.mediateca.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateca.domain.Playlist
import com.example.playlistmaker.mediateca.domain.PlaylistInteractor
import com.example.playlistmaker.mediateca.ui.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData
    public override fun onCleared() {
        super.onCleared()
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }

    fun fillData() {
        renderState(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.PlaylistContent(playlists))
        }
    }
}