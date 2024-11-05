package com.example.musicapp.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.repository.PlayListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlayListViewModel @Inject constructor(
    private val repository: PlayListRepository
) : ViewModel() {

    private val databaseScope = CoroutineScope(Dispatchers.IO)

    private var playListId: Long? = null

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    val localSongs = repository.getLocalPlayList()

    fun savePlaylist(name: String, selectedSongs: SnapshotStateList<Long>) = databaseScope.launch {
        playListId?.let {
            repository.savePlayList(it, name)
            repository.addSongs(it, selectedSongs.toList())
        }
    }

    fun loadPlaylist(id: Long) = databaseScope.launch {
        playListId = id
        repository.getPlayList(id)?.let {
            _name.value = it.name
        }
    }

    fun updatePlayListName(name: String) {
        _name.value = name
    }

}