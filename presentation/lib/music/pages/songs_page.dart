import "dart:io";
import "dart:ui";
import "package:flutter/material.dart";
import "package:music/song.dart";
import "package:presentation/music/widgets/widgets.dart";

import "package:shared_preferences/shared_preferences.dart";

import "package:flutter_bloc/flutter_bloc.dart";
import "package:presentation/music/presentation/cubit/player/player_cubit.dart";
import "package:presentation/music/presentation/cubit/songs/songs_cubit.dart";

class SongsPage extends StatefulWidget {
  const SongsPage({
    super.key,
  });

  @override
  State<SongsPage> createState() => _SongsPageState();
}

class _SongsPageState extends State<SongsPage> {
  String _searchQuery = "";
  final _searchController = TextEditingController();
  Set<String> _favoritePaths = {};

  @override
  void initState() {
    super.initState();
    _loadFavorites();
  }

  Future<void> _loadFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final favoritePaths = prefs.getStringList("favorite_songs") ?? [];
    if (mounted) {
      setState(() => _favoritePaths = favoritePaths.toSet());
    }
  }

  Future<void> _toggleFavorite(LocalSong song) async {
    final prefs = await SharedPreferences.getInstance();
    final favoritePaths = prefs.getStringList("favorite_songs") ?? [];

    if (favoritePaths.contains(song.path)) {
      favoritePaths.remove(song.path);
    } else {
      favoritePaths.add(song.path);
    }

    await prefs.setStringList("favorite_songs", favoritePaths);
    await _loadFavorites();
  }

  List<LocalSong> _filterSongs(List<LocalSong> songs) {
    if (_searchQuery.isEmpty) {
      return songs;
    }

    final query = _searchQuery.toLowerCase();
    return songs.where((song) {
      return song.title.toLowerCase().contains(query) ||
          song.artist.toLowerCase().contains(query);
    }).toList();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocSelector<PlayerCubit, PlayerState, Song?>(
      selector: (state) => state.currentSong,
      builder: (context, state) {
        final currentSong = state as LocalSong?;
        return Scaffold(
          backgroundColor: Colors.black,
          extendBodyBehindAppBar: true,
          body: Stack(
            children: [
              // Background with blur effect
              if (currentSong?.thumbnailPath != null)
                Positioned.fill(
                  child: Stack(
                    children: [
                      // Background image
                      Image.file(
                        File(currentSong!.thumbnailPath),
                        fit: BoxFit.cover,
                        width: double.infinity,
                        height: double.infinity,
                        errorBuilder: (_, __, ___) =>
                            Container(color: Colors.black),
                      ),
                      // Blur overlay
                      BackdropFilter(
                        filter: ImageFilter.blur(sigmaX: 30, sigmaY: 30),
                        child: Container(
                          color: Colors.black.withValues(alpha: 0.7),
                        ),
                      ),
                    ],
                  ),
                ),

              // Main content
              Column(
                children: [
                  const SizedBox(height: 20),

                  // Compact Search Bar
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    child: Container(
                      height: 45,
                      decoration: BoxDecoration(
                        color: Colors.white.withValues(alpha: 0.1),
                        borderRadius: BorderRadius.circular(25),
                        border: Border.all(
                          color: Colors.white.withValues(alpha: 0.2),
                          width: 1,
                        ),
                      ),
                      child: TextField(
                        controller: _searchController,
                        onChanged: (value) =>
                            setState(() => _searchQuery = value),
                        style: const TextStyle(color: Colors.white),
                        decoration: InputDecoration(
                          hintText: "Search songs, artists...",
                          hintStyle: TextStyle(
                            color: Colors.white.withValues(alpha: 0.6),
                            fontSize: 14,
                          ),
                          prefixIcon: Icon(
                            Icons.search,
                            color: Colors.white.withValues(alpha: 0.6),
                            size: 20,
                          ),
                          border: InputBorder.none,
                          contentPadding: const EdgeInsets.symmetric(
                            horizontal: 16,
                            vertical: 12,
                          ),
                        ),
                      ),
                    ),
                  ),

                  const SizedBox(height: 16),

                  // Songs List
                  Expanded(
                    child: _buildSongsList(),
                  ),
                ],
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildSongsList() {
    return BlocBuilder<SongsCubit, SongsState>(
      builder: (context, state) {
        final allSongs = state is SongsLoaded ? state.songs : <LocalSong>[];
        final songs = _filterSongs(allSongs);

        if (state is SongsLoading && allSongs.isEmpty) {
          return const Center(
            child: CircularProgressIndicator(color: Colors.white),
          );
        }

        if (allSongs.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.music_note_rounded,
                  size: 64,
                  color: Colors.white.withValues(alpha: 0.5),
                ),
                const SizedBox(height: 16),
                Text(
                  "No songs found",
                  style: TextStyle(
                    color: Colors.white.withValues(alpha: 0.8),
                    fontSize: 18,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  "Load your local music library to get started",
                  style: TextStyle(
                    color: Colors.white.withValues(alpha: 0.6),
                    fontSize: 14,
                  ),
                ),
                const SizedBox(height: 24),
                ElevatedButton.icon(
                  onPressed: () => context.read<SongsCubit>().loadSongs(),
                  icon: const Icon(Icons.refresh_rounded),
                  label: const Text("Load Songs"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    foregroundColor: Colors.black,
                  ),
                ),
              ],
            ),
          );
        }

        if (songs.isEmpty && _searchQuery.isNotEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.search_off_rounded,
                  size: 64,
                  color: Colors.white.withValues(alpha: 0.5),
                ),
                const SizedBox(height: 16),
                Text(
                  'No results for "$_searchQuery"',
                  style: TextStyle(
                    color: Colors.white.withValues(alpha: 0.8),
                    fontSize: 16,
                  ),
                ),
              ],
            ),
          );
        }

        return BlocBuilder<PlayerCubit, PlayerState>(
          builder: (context, playerState) {
            return Column(
              children: [
                // Song count
                if (_searchQuery.isEmpty)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    child: Row(
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 12,
                            vertical: 6,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white.withValues(alpha: 0.1),
                            borderRadius: BorderRadius.circular(20),
                            border: Border.all(
                              color: Colors.white.withValues(alpha: 0.2),
                            ),
                          ),
                          child: Text(
                            "${songs.length} songs",
                            style: TextStyle(
                              color: Colors.white.withValues(alpha: 0.8),
                              fontSize: 12,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                        const Spacer(),
                        if (state is SongsLoading)
                          SizedBox(
                            width: 16,
                            height: 16,
                            child: CircularProgressIndicator(
                              strokeWidth: 2,
                              color: Colors.white.withValues(alpha: 0.6),
                            ),
                          ),
                      ],
                    ),
                  ),
                const SizedBox(height: 12),

                // Songs list
                Expanded(
                  child: ListView.builder(
                    padding:
                        const EdgeInsets.only(left: 16, right: 16, bottom: 100),
                    itemCount: songs.length,
                    itemBuilder: (context, index) {
                      final song = songs[index];
                      final playerSong = playerState.currentSong;
                      final isPlaying = playerSong is LocalSong &&
                          playerSong.path == song.path &&
                          playerState.isPlaying;
                      final isFavorite = _favoritePaths.contains(song.path);

                      return Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: _buildSongItem(song, isPlaying, isFavorite),
                      );
                    },
                  ),
                ),
              ],
            );
          },
        );
      },
    );
  }

  Widget _buildSongItem(LocalSong song, bool isPlaying, bool isFavorite) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white.withValues(alpha: isPlaying ? 0.15 : 0.08),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: isPlaying
              ? Colors.white.withValues(alpha: 0.3)
              : Colors.white.withValues(alpha: 0.1),
        ),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        leading: Container(
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(8),
            color: Colors.white.withValues(alpha: 0.1),
          ),
          child: Stack(
            children: [
              Image.file(
                File(song.thumbnailPath),
                opacity: AlwaysStoppedAnimation(0.5),
                fit: BoxFit.cover,
                width: 50,
                height: 50,
                errorBuilder: (_, __, ___) {
                  return Container(
                    color: Colors.grey.withValues(alpha: 0.3),
                  );
                },
              ),
              if (isPlaying)
                Container(
                  alignment: Alignment.center,
                  decoration: BoxDecoration(
                    color: Colors.black.withValues(alpha: 0.3),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: AnimatedEqualizer(),
                ),
            ],
          ),
        ),
        title: Text(
          song.title,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            color:
                isPlaying ? Colors.white : Colors.white.withValues(alpha: 0.9),
            fontWeight: isPlaying ? FontWeight.w600 : FontWeight.w500,
            fontSize: 15,
          ),
        ),
        subtitle: Text(
          song.artist,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            color: Colors.white.withValues(alpha: 0.6),
            fontSize: 13,
          ),
        ),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (song.durationMillis > 0)
              Text(
                _formatDuration(song.durationMillis),
                style: TextStyle(
                  color: Colors.white.withValues(alpha: 0.6),
                  fontSize: 12,
                ),
              ),
            const SizedBox(width: 8),
            IconButton(
              icon: Icon(
                isFavorite ? Icons.favorite : Icons.favorite_border,
                color: isFavorite
                    ? Colors.red[400]
                    : Colors.white.withValues(alpha: 0.6),
                size: 20,
              ),
              onPressed: () => _toggleFavorite(song),
            ),
          ],
        ),
        onTap: () => context.read<PlayerCubit>().playSong(song),
      ),
    );
  }

  String _formatDuration(int milliseconds) {
    final duration = Duration(milliseconds: milliseconds);
    final minutes = duration.inMinutes;
    final seconds = duration.inSeconds % 60;
    return '$minutes:${seconds.toString().padLeft(2, '0')}';
  }
}
