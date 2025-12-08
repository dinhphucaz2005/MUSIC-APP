import 'package:flutter/material.dart';
import 'package:music/local_song_repository.dart';
import 'package:music/media_controller_manager.dart';
import 'package:music/song.dart';
import 'package:presentation/music/widgets/widgets.dart';

import 'package:shared_preferences/shared_preferences.dart';

class FavoritesPage extends StatefulWidget {
  final LocalSongRepository repository;
  final MediaControllerManager mediaController;

  const FavoritesPage({
    super.key,
    required this.repository,
    required this.mediaController,
  });

  @override
  State<FavoritesPage> createState() => _FavoritesPageState();
}

class _FavoritesPageState extends State<FavoritesPage> {
  List<LocalSong> _favoriteSongs = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadFavorites();
  }

  Future<void> _loadFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final favoritePaths = prefs.getStringList('favorite_songs') ?? [];

    widget.repository.subject.listen((state) {
      final allSongs = state.songs;
      final favorites = allSongs.where((song) => favoritePaths.contains(song.path)).toList();

      if (mounted) {
        setState(() {
          _favoriteSongs = favorites;
          _isLoading = false;
        });
      }
    });
  }

  Future<void> _toggleFavorite(LocalSong song) async {
    final prefs = await SharedPreferences.getInstance();
    final favoritePaths = prefs.getStringList('favorite_songs') ?? [];

    if (favoritePaths.contains(song.path)) {
      favoritePaths.remove(song.path);
    } else {
      favoritePaths.add(song.path);
    }

    await prefs.setStringList('favorite_songs', favoritePaths);
    await _loadFavorites();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Favorites'),
        actions: [
          if (_favoriteSongs.isNotEmpty) ...[
            IconButton(
              icon: const Icon(Icons.shuffle_rounded),
              onPressed: () {
                final shuffled = List<LocalSong>.from(_favoriteSongs)..shuffle();
                if (shuffled.isNotEmpty) {
                  widget.mediaController.playSong(shuffled.first);
                }
              },
            ),
            IconButton(
              icon: const Icon(Icons.play_arrow_rounded),
              onPressed: () {
                if (_favoriteSongs.isNotEmpty) {
                  widget.mediaController.playSong(_favoriteSongs.first);
                }
              },
            ),
          ],
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _favoriteSongs.isEmpty
              ? const EmptyState(
                  icon: Icons.favorite_rounded,
                  title: 'No favorites yet',
                  subtitle: 'Tap the heart icon on songs to add them here',
                )
              : StreamBuilder<PlayerState>(
                  stream: widget.mediaController.subject,
                  builder: (context, playerSnapshot) {
                    final playerState = playerSnapshot.data;

                    return ListView.builder(
                      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                      itemCount: _favoriteSongs.length,
                      itemBuilder: (context, index) {
                        final song = _favoriteSongs[index];
                        final playerSong = playerState?.currentSong;
                        final isPlaying =
                            playerState != null && playerSong is LocalSong && playerSong.title == song.title && (playerState.isPlaying ?? false);

                        return Padding(
                          padding: const EdgeInsets.only(bottom: 8),
                          child: SongItem(
                            song: song,
                            isPlaying: isPlaying,
                            isFavorite: true,
                            onTap: () => widget.mediaController.playSong(song),
                            onFavoriteToggle: () => _toggleFavorite(song),
                          ),
                        );
                      },
                    );
                  },
                ),
    );
  }
}
