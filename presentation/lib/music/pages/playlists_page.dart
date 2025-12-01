import 'package:flutter/material.dart';

class PlaylistsPage extends StatefulWidget {
  const PlaylistsPage({super.key});

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
          decoration: const InputDecoration(
            labelText: 'Playlist Name',
            hintText: 'Enter playlist name',
          ),
          autofocus: true,
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () {
              if (controller.text.trim().isNotEmpty) {
                setState(() {
                  _playlists.add(Playlist(
                    id: DateTime.now().millisecondsSinceEpoch.toString(),
                    name: controller.text.trim(),
                    songCount: 0,
                    imageUrl: null,
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

  void _deletePlaylist(String id) {
    setState(() {
      _playlists.removeWhere((p) => p.id == id);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Playlists'),
      ),
      body: _playlists.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.playlist_play, size: 80, color: Colors.grey[400]),
                  const SizedBox(height: 24),
                  Text(
                    'No playlists yet',
                    style: TextStyle(
                      fontSize: 20,
                      color: Colors.grey[600],
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Create your first playlist',
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.grey[500],
                    ),
                  ),
                ],
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.symmetric(vertical: 8),
              itemCount: _playlists.length,
              itemBuilder: (context, index) {
                final playlist = _playlists[index];
                return ListTile(
                  leading: Container(
                    width: 56,
                    height: 56,
                    decoration: BoxDecoration(
                      color: Colors.primaries[index % Colors.primaries.length]
                          .withValues(alpha: 0.2),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Icon(
                      Icons.playlist_play,
                      color: Colors.primaries[index % Colors.primaries.length],
                      size: 32,
                    ),
                  ),
                  title: Text(
                    playlist.name,
                    style: const TextStyle(fontWeight: FontWeight.w600),
                  ),
                  subtitle: Text('${playlist.songCount} songs'),
                  trailing: playlist.id != '1'
                      ? IconButton(
                          icon: const Icon(Icons.more_vert),
                          onPressed: () {
                            showModalBottomSheet(
                              context: context,
                              builder: (context) => SafeArea(
                                child: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    ListTile(
                                      leading: const Icon(Icons.edit),
                                      title: const Text('Rename'),
                                      onTap: () {
                                        Navigator.pop(context);
                                        // TODO: Implement rename
                                      },
                                    ),
                                    ListTile(
                                      leading: const Icon(
                                        Icons.delete,
                                        color: Colors.red,
                                      ),
                                      title: const Text(
                                        'Delete',
                                        style: TextStyle(color: Colors.red),
                                      ),
                                      onTap: () {
                                        Navigator.pop(context);
                                        _deletePlaylist(playlist.id);
                                      },
                                    ),
                                  ],
                                ),
                              ),
                            );
                          },
                        )
                      : null,
                  onTap: () {
                    // TODO: Open playlist detail
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text('Opening ${playlist.name}...'),
                        duration: const Duration(seconds: 1),
                      ),
                    );
                  },
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: _createPlaylist,
        child: const Icon(Icons.add),
      ),
    );
  }
}

class Playlist {
  final String id;
  final String name;
  final int songCount;
  final String? imageUrl;

  Playlist({
    required this.id,
    required this.name,
    required this.songCount,
    this.imageUrl,
  });
}

