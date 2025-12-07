import 'package:presentation/music/domain/local_song_repository.dart';
import 'package:presentation/music/domain/media_controller_manager.dart';
import 'package:shadcn_flutter/shadcn_flutter.dart';

class PlaylistsPage extends StatefulWidget {
  final LocalSongRepository repository;
  final MediaControllerManager mediaController;

  const PlaylistsPage({
    super.key,
    required this.repository,
    required this.mediaController,
  });

  @override
  State<PlaylistsPage> createState() => _PlaylistsPageState();
}

class _PlaylistsPageState extends State<PlaylistsPage> {
  final List<Playlist> _playlists = [
    Playlist(
      id: '1',
      name: 'Favorites',
      songCount: 0,
      imageUrl: null,
      isSystem: true,
    ),
  ];

  void _createPlaylist() {
    final controller = TextEditingController();
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Create Playlist'),
        content: TextField(
          controller: controller,
          placeholder: const Text('Enter playlist name'),
          autofocus: true,
        ),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          Button.primary(
            onPressed: () {
              if (controller.text.trim().isNotEmpty) {
                setState(() {
                  _playlists.add(Playlist(
                    id: DateTime.now().millisecondsSinceEpoch.toString(),
                    name: controller.text.trim(),
                    songCount: 0,
                    imageUrl: null,
                    isSystem: false,
                  ));
                });
                Navigator.pop(context);
              }
            },
            child: const Text('Create'),
          ),
        ],
      ),
    );
  }

  void _showPlaylistOptions(BuildContext context, Playlist playlist) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(playlist.name),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            GestureDetector(
              onTap: () {
                Navigator.pop(context);
                _renamePlaylist(playlist);
              },
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 12),
                child: Row(
                  children: [
                    const Icon(Icons.edit_rounded),
                    const SizedBox(width: 12),
                    const Text('Rename'),
                  ],
                ),
              ),
            ),
            GestureDetector(
              onTap: () {
                Navigator.pop(context);
                _confirmDelete(playlist);
              },
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 12),
                child: Row(
                  children: [
                    Icon(Icons.delete_rounded, color: Colors.red),
                    const SizedBox(width: 12),
                    Text('Delete', style: TextStyle(color: Colors.red)),
                  ],
                ),
              ),
            ),
          ],
        ),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
        ],
      ),
    );
  }

  void _renamePlaylist(Playlist playlist) {
    final controller = TextEditingController(text: playlist.name);
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Rename Playlist'),
        content: TextField(
          controller: controller,
          placeholder: const Text('Enter new name'),
          autofocus: true,
        ),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          Button.primary(
            onPressed: () {
              if (controller.text.trim().isNotEmpty) {
                setState(() {
                  final index = _playlists.indexWhere((p) => p.id == playlist.id);
                  if (index != -1) {
                    _playlists[index] = Playlist(
                      id: playlist.id,
                      name: controller.text.trim(),
                      songCount: playlist.songCount,
                      imageUrl: playlist.imageUrl,
                      isSystem: playlist.isSystem,
                    );
                  }
                });
                Navigator.pop(context);
              }
            },
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }

  void _confirmDelete(Playlist playlist) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete Playlist'),
        content: Text('Are you sure you want to delete "${playlist.name}"?'),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          Button.destructive(
            onPressed: () {
              setState(() {
                _playlists.removeWhere((p) => p.id == playlist.id);
              });
              Navigator.pop(context);
            },
            child: const Text('Delete'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      headers: [
        AppBar(
          title: const Text('Playlists'),
          trailing: [
            IconButton.ghost(
              icon: const Icon(Icons.add_rounded),
              onPressed: _createPlaylist,
            ),
          ],
        ),
      ],
      child: _playlists.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.playlist_play_rounded,
                    size: 80,
                    color: theme.colorScheme.mutedForeground,
                  ),
                  const SizedBox(height: 24),
                  Text(
                    'No playlists yet',
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.w600,
                      color: theme.colorScheme.mutedForeground,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Create your first playlist',
                    style: TextStyle(
                      fontSize: 14,
                      color: theme.colorScheme.mutedForeground.withValues(alpha: 0.7),
                    ),
                  ),
                  const SizedBox(height: 24),
                  Button.primary(
                    onPressed: _createPlaylist,
                    leading: const Icon(Icons.add_rounded),
                    child: const Text('Create Playlist'),
                  ),
                ],
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
              itemCount: _playlists.length,
              itemBuilder: (context, index) {
                final playlist = _playlists[index];
                final colorIndex = index % Colors.primaries.length;

                return Padding(
                  padding: const EdgeInsets.only(bottom: 8),
                  child: Card(
                    child: GestureDetector(
                      onTap: () {
                        showToast(
                          context: context,
                          builder: (context, overlay) => SurfaceCard(
                            child: Basic(
                              title: Text('Opening ${playlist.name}...'),
                            ),
                          ),
                        );
                      },
                      child: Padding(
                        padding: const EdgeInsets.all(12),
                        child: Row(
                          children: [
                            Container(
                              width: 56,
                              height: 56,
                              decoration: BoxDecoration(
                                color: Colors.primaries[colorIndex].withValues(alpha: 0.2),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Icon(
                                playlist.isSystem ? Icons.favorite_rounded : Icons.playlist_play_rounded,
                                color: Colors.primaries[colorIndex],
                                size: 28,
                              ),
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    playlist.name,
                                    style: const TextStyle(fontWeight: FontWeight.w600),
                                  ),
                                  Text(
                                    '${playlist.songCount} songs',
                                    style: TextStyle(
                                      fontSize: 13,
                                      color: theme.colorScheme.mutedForeground,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            if (!playlist.isSystem)
                              IconButton.ghost(
                                icon: const Icon(Icons.more_vert_rounded),
                                onPressed: () => _showPlaylistOptions(context, playlist),
                              ),
                          ],
                        ),
                      ),
                    ),
                  ),
                );
              },
            ),
    );
  }
}

class Playlist {
  final String id;
  final String name;
  final int songCount;
  final String? imageUrl;
  final bool isSystem;

  Playlist({
    required this.id,
    required this.name,
    required this.songCount,
    this.imageUrl,
    this.isSystem = false,
  });
}
