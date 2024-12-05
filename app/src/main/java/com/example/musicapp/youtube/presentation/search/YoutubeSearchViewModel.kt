package com.example.musicapp.youtube.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.youtube.domain.YoutubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.musicapp.core.domain.Result


@HiltViewModel
class YoutubeSearchViewModel @Inject constructor(
    private val youtubeRepository: YoutubeRepository
) : ViewModel() {

    private val _songListState = MutableStateFlow(SongListState(searchQuery =  "Cho nhau lối thoát remix"))
    val songListState: StateFlow<SongListState> = _songListState.asStateFlow()

    fun onSearchQueryChange(searchQuery: String) = viewModelScope.launch {
        _songListState.update { it.copy(searchQuery = searchQuery) }
    }

    fun onSearch() = viewModelScope.launch {
        when (val result = youtubeRepository.searchSongs(_songListState.value.searchQuery)) {
            is Result.Success -> {
                _songListState.update { it.copy(searchResults = result.data) }
            }

            is Result.Error -> {
                _songListState.update { it.copy(errorMessage = result.error.name) }
            }
        }
    }


}