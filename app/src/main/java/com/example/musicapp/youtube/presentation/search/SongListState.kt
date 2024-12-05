package com.example.musicapp.youtube.presentation.search

import androidx.compose.runtime.Immutable
import com.example.innertube.models.SongItem

@Immutable
data class SongListState(
    val searchQuery: String = "Cho nhau lối thoát remix Vũ Duy Khánh",
    val searchResults: List<SongItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: String? = null
)

