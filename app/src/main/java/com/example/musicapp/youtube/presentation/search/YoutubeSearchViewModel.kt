package com.example.musicapp.youtube.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.CustomYoutube
import com.example.innertube.YouTube
import com.example.innertube.models.SongItem
import com.example.musicapp.extension.withIOContext
import com.example.musicapp.other.data.database.entity.SearchEntity
import com.example.musicapp.other.domain.repository.CacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class YoutubeSearchViewModel @Inject constructor(
    private val cacheRepository: CacheRepository
) : ViewModel() {


    val recentSearches: StateFlow<List<SearchEntity>> = cacheRepository.getRecentSearches()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _songListState =
        MutableStateFlow(SongListState(searchQuery = "Cho nhau lối thoát remix"))
    val songListState: StateFlow<SongListState> = _songListState.asStateFlow()

    fun onSearchQueryChange(searchQuery: String) =
        _songListState.update { it.copy(searchQuery = searchQuery) }

    fun onSearch() = viewModelScope.launch {
        CustomYoutube.search(_songListState.value.searchQuery, YouTube.SearchFilter.FILTER_VIDEO)
            .onSuccess { searchResults ->
                val songs = searchResults.items.filterIsInstance<SongItem>()
                withIOContext {
                    cacheRepository.insertSearch(
                        query = _songListState.value.searchQuery,
                        result = songs
                    )
                }
                _songListState.update { it.copy(searchResults = songs) }
            }
    }

    fun onRecentSearchClicked(search: SearchEntity) {
        val songs = search.toItem().filterIsInstance<SongItem>()
        if (songs.isNotEmpty()) {
            _songListState.update { it.copy(searchResults = songs) }
        } else {
            _songListState.update { it.copy(searchQuery = search.searchQuery ?: "") }
            onSearch()
        }

    }


}