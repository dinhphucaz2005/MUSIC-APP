package com.example.musicapp.youtube.presentation.search

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.innertube.CustomYoutube
//import com.example.innertube.YouTube
//import com.example.innertube.models.SongItem
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject


//@HiltViewModel
//class YoutubeSearchViewModel @Inject constructor() : ViewModel() {
//
//
//    private val _songListState = MutableStateFlow(SongListState(searchQuery =  "Cho nhau lối thoát remix"))
//    val songListState: StateFlow<SongListState> = _songListState.asStateFlow()
//
//    fun onSearchQueryChange(searchQuery: String) =
//        _songListState.update { it.copy(searchQuery = searchQuery) }
//
//    fun onSearch() = viewModelScope.launch {
//        CustomYoutube.search(_songListState.value.searchQuery, YouTube.SearchFilter.FILTER_VIDEO)
//            .onSuccess { searchResults ->
//                _songListState.update { it.copy(searchResults = searchResults.items.filterIsInstance<SongItem>()) }
//            }
//    }
//
//
//}