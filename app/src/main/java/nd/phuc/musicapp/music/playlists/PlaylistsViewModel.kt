package nd.phuc.musicapp.music.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.LikedSongsPlaylist
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.Song
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository

class PlaylistsViewModel(
    private val songRepository: LocalSongRepository,
) : ViewModel() {

    val allSongs: StateFlow<List<LocalSong>> = songRepository.allSongs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val playlists: StateFlow<List<Playlist<LocalSong>>> = songRepository.playlist
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val likedPlaylist: StateFlow<LikedSongsPlaylist<Song>> = songRepository.likedSongs
        .map { LikedSongsPlaylist<Song>(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LikedSongsPlaylist(emptyList())
        )


    private val _selectedPlaylistId = MutableStateFlow<Long?>(null)
    val selectedPlaylistId: StateFlow<Long?> = _selectedPlaylistId
    val selectedPlaylist: StateFlow<Playlist<LocalSong>?> = combine(
        playlists, _selectedPlaylistId
    ) { playlists, selectedId ->
        playlists.firstOrNull { it.id == selectedId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun addSongToPlaylist(song: LocalSong) {
        val playlistId = _selectedPlaylistId.value ?: return
        viewModelScope.launch {
            songRepository.addSongToPlaylist(playlistId, song)
        }
    }

    fun addPlaylist(name: String) {
        viewModelScope.launch {
            songRepository.createPlaylist(name)
        }
    }

    fun selectPlaylist(playlistId: Long?) {
        _selectedPlaylistId.value = playlistId
    }
}
