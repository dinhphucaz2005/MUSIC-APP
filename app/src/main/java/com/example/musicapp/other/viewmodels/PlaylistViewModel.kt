package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musicapp.extension.load
import com.example.musicapp.extension.withIOContext
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.domain.model.Playlist
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    data class PlaylistItem(
        val data: Playlist, val isSelected: Boolean = false
    )


    private val _isLoading = MutableStateFlow(false)
    private val _playlists = MutableStateFlow<List<PlaylistItem>>(emptyList())
    private val _favouritePlaylist = MutableStateFlow(
        Playlist(
            id = PlaylistEntity.LIKED_PLAYLIST_ID,
            name = "",
            songs = emptyList()
        )
    )
    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)

    val currentPlaylist = _currentPlaylist.asStateFlow()
    val favouritePlaylist = _favouritePlaylist.asStateFlow()
    val playlists = _playlists.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    fun loadPlayLists() = load(_isLoading) {
        withIOContext {
            val playlists = songRepository.getPlayLists()
            _playlists.value = playlists.map { PlaylistItem(it) }

            val songs = songRepository.getSongsFromPlaylist(PlaylistEntity.LIKED_PLAYLIST_ID)
            _favouritePlaylist.update { it.copy(songs = songs) }
        }
    }

    fun createNewPlayList(name: String) {
        withIOContext {
            songRepository.createPlayList(name)
            loadPlayLists()
        }
    }

    fun getPlaylist(playlistId: Int) {
        withIOContext {
            val songs = songRepository.getSongsFromPlaylist(playlistId)
            val playlistItem = _playlists.value.find { it.data.id == playlistId }
            playlistItem?.let {
                _currentPlaylist.value = Playlist(
                    id = it.data.id, name = it.data.name, songs = songs
                )
            }
        }
    }


    fun removeFromPlaylist(song: Song) = load {
        songRepository.deleteSongs(listOf(song.id))
        loadPlayLists()
    }

}