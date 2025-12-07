import 'package:music/local_song_repository.dart';
import 'package:music/media_controller_manager.dart';
import 'package:presentation/music/widgets/widgets.dart';
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
    Playlist(id: '1', name: 'Favorites', songCount: 0, isSystem: true),
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

  void _showPlaylistOptions(Playlist playlist) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(playlist.name),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            _OptionTile(
              icon: Icons.edit_rounded,
              label: 'Rename',
              onTap: () {
                Navigator.pop(context);
                _renamePlaylist(playlist);
              },
            ),
            _OptionTile(
              icon: Icons.delete_rounded,
              label: 'Delete',
              isDestructive: true,
              onTap: () {
                Navigator.pop(context);
                _confirmDelete(playlist);
              },
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
                    _playlists[index] = playlist.copyWith(name: controller.text.trim());
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
              setState(() => _playlists.removeWhere((p) => p.id == playlist.id));
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
          ? EmptyState(
              icon: Icons.playlist_play_rounded,
              title: 'No playlists yet',
              subtitle: 'Create your first playlist',
              action: Button.primary(
                onPressed: _createPlaylist,
                leading: const Icon(Icons.add_rounded),
                child: const Text('Create Playlist'),
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
                  child: ListTileCard(
                    margin: EdgeInsets.zero,
                    leading: SquareAvatar(
                      icon: playlist.isSystem ? Icons.favorite_rounded : Icons.playlist_play_rounded,
                      backgroundColor: Colors.primaries[colorIndex].withValues(alpha: 0.2),
                      iconColor: Colors.primaries[colorIndex],
                    ),
                    title: playlist.name,
                    subtitle: '${playlist.songCount} songs',
                    trailing: playlist.isSystem
                        ? null
                        : IconButton.ghost(
                            icon: const Icon(Icons.more_vert_rounded),
                            onPressed: () => _showPlaylistOptions(playlist),
                          ),
                    onTap: () {
                      showToast(
                        context: context,
                        builder: (context, overlay) => SurfaceCard(
                          child: Basic(title: Text('Opening ${playlist.name}...')),
                        ),
                      );
                    },
                  ),
                );
              },
            ),
    );
  }
}

class _OptionTile extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;
  final bool isDestructive;

  const _OptionTile({
    required this.icon,
    required this.label,
    required this.onTap,
    this.isDestructive = false,
  });

  @override
  Widget build(BuildContext context) {
    final color = isDestructive ? Colors.red : null;
    return GestureDetector(
      onTap: onTap,
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 12),
        child: Row(
          children: [
            Icon(icon, color: color),
            const SizedBox(width: 12),
            Text(label, style: TextStyle(color: color)),
          ],
        ),
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

  Playlist copyWith({String? name, int? songCount}) {
    return Playlist(
      id: id,
      name: name ?? this.name,
      songCount: songCount ?? this.songCount,
      imageUrl: imageUrl,
      isSystem: isSystem,
    );
  }
}
