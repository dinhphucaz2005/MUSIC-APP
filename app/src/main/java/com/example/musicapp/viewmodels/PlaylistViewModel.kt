package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.repository.PlayListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val repository: PlayListRepository,
) : ViewModel() {

    private val databaseScope = CoroutineScope(Dispatchers.IO)

    val playlists = repository.getSavedPlayLists()

    fun createNewPlayList(name: String) = databaseScope.launch {
        repository.createPlayList(name)
    }

    fun deletePlayList(id: Long) = databaseScope.launch {
        repository.deletePlayList(id)
    }
}