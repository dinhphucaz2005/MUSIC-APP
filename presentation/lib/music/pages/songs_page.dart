import 'package:presentation/music/domain/local_song_repository.dart';
import 'package:presentation/music/domain/media_controller_manager.dart';
import 'package:presentation/music/domain/song.dart';
import 'package:shadcn_flutter/shadcn_flutter.dart';
import 'package:presentation/music/widgets/song_item.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SongsPage extends StatefulWidget {
  final LocalSongRepository repository;
  final MediaControllerManager mediaController;

  const SongsPage({super.key, required this.repository, required this.mediaController});

  @override
  State<SongsPage> createState() => _SongsPageState();
}

class _SongsPageState extends State<SongsPage> {
  String _searchQuery = '';
  final _searchController = TextEditingController();
  Set<String> _favoritePaths = {};

  @override
  void initState() {
    super.initState();
    _loadFavorites();
  }

  Future<void> _loadFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final favoritePaths = prefs.getStringList('favorite_songs') ?? [];
    if (mounted) {
      setState(() => _favoritePaths = favoritePaths.toSet());
    }
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

  List<LocalSong> _filterSongs(List<LocalSong> songs) {
    if (_searchQuery.isEmpty) return songs;

    final query = _searchQuery.toLowerCase();
    return songs.where((song) {
      return song.title.toLowerCase().contains(query) || song.artist.toLowerCase().contains(query);
    }).toList();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      headers: [
        AppBar(
          title: const Text('My Music'),
          trailing: [
            IconButton.ghost(
              icon: const Icon(Icons.refresh_rounded),
              onPressed: () => widget.repository.loadLocalSongs(),
            ),
          ],
        ),
      ],
      child: Column(
        children: [
          // Search Bar
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _searchController,
              placeholder: const Text('Search songs, artists...'),
              onChanged: (value) {
                setState(() => _searchQuery = value);
              },
            ),
          ),

          // Songs List
          Expanded(
            child: StreamBuilder<LocalSongRepositoryState>(
              stream: widget.repository.subject,
              builder: (context, snapshot) {
                final state = snapshot.data ?? LocalSongRepositoryState();
                final allSongs = state.songs;
                final songs = _filterSongs(allSongs);

                if (state.isLoading && allSongs.isEmpty) {
                  return const Center(child: CircularProgressIndicator());
                }

                if (allSongs.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          padding: const EdgeInsets.all(24),
                          decoration: BoxDecoration(
                            color: theme.colorScheme.primary.withValues(alpha: 0.1),
                            shape: BoxShape.circle,
                          ),
                          child: Icon(
                            Icons.music_note_rounded,
                            size: 64,
                            color: theme.colorScheme.primary,
                          ),
                        ),
                        const SizedBox(height: 24),
                        Text(
                          'No songs found',
                          style: TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                            color: theme.colorScheme.foreground,
                          ),
                        ),
                        const SizedBox(height: 8),
                        Text(
                          'Load your local music library to get started',
                          style: TextStyle(
                            fontSize: 16,
                            color: theme.colorScheme.mutedForeground,
                          ),
                        ),
                        const SizedBox(height: 32),
                        Button.primary(
                          onPressed: () => widget.repository.loadLocalSongs(),
                          leading: const Icon(Icons.refresh_rounded),
                          child: const Text('Load Songs'),
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
                          color: theme.colorScheme.mutedForeground,
                        ),
                        const SizedBox(height: 16),
                        Text(
                          'No results for "$_searchQuery"',
                          style: TextStyle(
                            fontSize: 18,
                            color: theme.colorScheme.mutedForeground,
                          ),
                        ),
                      ],
                    ),
                  );
                }

                return StreamBuilder<PlayerState>(
                  stream: widget.mediaController.subject,
                  builder: (context, playerSnapshot) {
                    final playerState = playerSnapshot.data;

                    return Column(
                      children: [
                        // Song count
                        if (_searchQuery.isEmpty)
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            child: Row(
                              children: [
                                Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: theme.colorScheme.primary.withValues(alpha: 0.1),
                                    borderRadius: BorderRadius.circular(12),
                                  ),
                                  child: Text(
                                    '${songs.length} songs',
                                    style: TextStyle(
                                      fontSize: 12,
                                      fontWeight: FontWeight.w600,
                                      color: theme.colorScheme.primary,
                                    ),
                                  ),
                                ),
                                const Spacer(),
                                if (state.isLoading)
                                  const SizedBox(
                                    width: 16,
                                    height: 16,
                                    child: CircularProgressIndicator(strokeWidth: 2),
                                  ),
                              ],
                            ),
                          ),
                        const SizedBox(height: 8),

                        // Songs list
                        Expanded(
                          child: ListView.builder(
                            padding: const EdgeInsets.only(
                              left: 16,
                              right: 16,
                              bottom: 100,
                            ),
                            itemCount: songs.length,
                            itemBuilder: (context, index) {
                              final song = songs[index];
                              final playerSong = playerState?.currentSong;
                              final isPlaying = playerState != null &&
                                  playerSong is LocalSong &&
                                  playerSong.title == song.title &&
                                  (playerState.isPlaying ?? false);
                              final isFavorite = _favoritePaths.contains(song.path);

                              return Padding(
                                padding: const EdgeInsets.only(bottom: 8),
                                child: SongItem(
                                  song: song,
                                  isPlaying: isPlaying,
                                  isFavorite: isFavorite,
                                  onTap: () => widget.mediaController.playSong(song),
                                  onFavoriteToggle: () => _toggleFavorite(song),
                                ),
                              );
                            },
                          ),
                        ),
                      ],
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
