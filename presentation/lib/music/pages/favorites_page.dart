import 'package:presentation/music/domain/local_song_repository.dart';
import 'package:presentation/music/domain/media_controller_manager.dart';
import 'package:presentation/music/domain/song.dart';
import 'package:presentation/music/widgets/song_item.dart';
import 'package:shadcn_flutter/shadcn_flutter.dart';
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
  Set<String> _favoritePaths = {}; // ignore: unused_field
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
          _favoritePaths = favoritePaths.toSet();
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
    final theme = Theme.of(context);

    return Scaffold(
      headers: [
        AppBar(
          title: const Text('Favorites'),
          trailing: [
            if (_favoriteSongs.isNotEmpty) ...[
              IconButton.ghost(
                icon: const Icon(Icons.shuffle_rounded),
                onPressed: () {
                  final shuffled = List<LocalSong>.from(_favoriteSongs)..shuffle();
                  if (shuffled.isNotEmpty) {
                    widget.mediaController.playSong(shuffled.first);
                  }
                },
              ),
              IconButton.ghost(
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
      ],
      child: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _favoriteSongs.isEmpty
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.favorite_rounded,
                        size: 80,
                        color: theme.colorScheme.mutedForeground,
                      ),
                      const SizedBox(height: 16),
                      Text(
                        'No favorites yet',
                        style: TextStyle(
                          fontSize: 18,
                          color: theme.colorScheme.mutedForeground,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'Tap the heart icon on songs to add them here',
                        style: TextStyle(
                          fontSize: 14,
                          color: theme.colorScheme.mutedForeground.withValues(alpha: 0.7),
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
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
