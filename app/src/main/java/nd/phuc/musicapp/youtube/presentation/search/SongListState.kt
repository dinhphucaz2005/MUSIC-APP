package nd.phuc.musicapp.youtube.presentation.search

import androidx.compose.runtime.Immutable

@Immutable
data class SongListState(
    val searchQuery: String = "",
    val searchCurrents: List<String> = emptyList(),
    val searchSuggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: String? = null
)

