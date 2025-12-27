package nd.phuc.musicapp.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nd.phuc.musicapp.helper.ExtractLocalSongResult
import nd.phuc.musicapp.helper.LocalSongExtractor
import nd.phuc.musicapp.model.LocalSong

data class SongRepositoryState(
    val songs: List<LocalSong> = emptyList(),
    val isLoading: Boolean = false,
)

class SongRepository {
    private val _state = MutableStateFlow(SongRepositoryState())
    val state: StateFlow<SongRepositoryState> = _state.asStateFlow()

    fun loadSongs(context: Context) {
        _state.value = _state.value.copy(isLoading = true)
        LocalSongExtractor.initialize(context)
        LocalSongExtractor.extracts(
            context = context, filePaths = _state.value.songs.map { it.filePath }) { result ->
            when (result) {
                is ExtractLocalSongResult.Success -> {
                    val newSongs = _state.value.songs.toMutableList().apply {
                            if (any { it.filePath == result.song.filePath }) {
                                return@apply
                            }
                            add(result.song)
                            sortedBy { it.title }
                        }
                    _state.value = _state.value.copy(songs = newSongs)
                }

                is ExtractLocalSongResult.Finished -> {
                    _state.value = _state.value.copy(isLoading = false)
                }

                else -> {}
            }
        }
    }
}
