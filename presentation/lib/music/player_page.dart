import 'dart:io';
import 'package:flutter/material.dart';
import 'package:music/local_song_repository.dart';

import 'package:music/media_controller_manager.dart';

class PlayerPage extends StatefulWidget {
  const PlayerPage({super.key});

  @override
  State<PlayerPage> createState() => _PlayerPageState();
}

class _PlayerPageState extends State<PlayerPage> {
  final _repository = LocalSongRepository();
  final _mediaController = MediaControllerManager();

  @override
  void initState() {
    super.initState();
  }

  Future<void> _loadLocalSongs() async {
    _repository.loadLocalSongs();
  }

  Widget _buildThumbnail(String? thumbnailPath) {
    if (thumbnailPath == null || thumbnailPath.isEmpty) {
      return Container(
        width: 56,
        height: 56,
        decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(8)),
        child: Icon(Icons.music_note, color: Colors.grey[600]),
      );
    }

    // File path (starts with /)
    if (thumbnailPath.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(8),
        child: Image.file(
          File(thumbnailPath),
          width: 56,
          height: 56,
          fit: BoxFit.cover,
          errorBuilder:
              (_, __, ___) => Container(
                width: 56,
                height: 56,
                decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(8)),
                child: Icon(Icons.broken_image, color: Colors.grey[600]),
              ),
        ),
      );
    }

    // Network URL
    if (thumbnailPath.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(8),
        child: Image.network(
          thumbnailPath,
          width: 56,
          height: 56,
          fit: BoxFit.cover,
          errorBuilder:
              (_, __, ___) => Container(
                width: 56,
                height: 56,
                decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(8)),
                child: Icon(Icons.broken_image, color: Colors.grey[600]),
              ),
        ),
      );
    }

    // Fallback
    return Container(
      width: 56,
      height: 56,
      decoration: BoxDecoration(color: Colors.grey[300], borderRadius: BorderRadius.circular(8)),
      child: Icon(Icons.error, color: Colors.red[400]),
    );
  }

  String _formatDuration(int durationMillis) {
    final seconds = durationMillis ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('My Music'), actions: [IconButton(icon: Icon(Icons.refresh), onPressed: _loadLocalSongs)]),
      body: Column(
        children: [
          Expanded(
            child: StreamBuilder<LocalSongRepositoryState>(
              stream: _repository.subject,
              builder: (context, snapshot) {
                final state = snapshot.data ?? LocalSongRepositoryState();
                final songs = state.songs;

                if (state.isLoading && songs.isEmpty) {
                  return Center(child: CircularProgressIndicator());
                }

                if (songs.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.music_note, size: 64, color: Colors.grey),
                        SizedBox(height: 16),
                        Text('No songs loaded', style: TextStyle(fontSize: 18, color: Colors.grey)),
                        SizedBox(height: 16),
                        ElevatedButton.icon(onPressed: _loadLocalSongs, icon: Icon(Icons.library_music), label: Text('Load Local Songs')),
                      ],
                    ),
                  );
                }

                return Column(
                  children: [
                    if (state.isLoading) LinearProgressIndicator(),
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Text('${songs.length} songs', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                    Expanded(
                      child: ListView.builder(
                        itemCount: songs.length,
                        itemBuilder: (context, index) {
                          final song = songs[index];
                          return ListTile(
                            leading: _buildThumbnail(song.thumbnailPath),
                            title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis),
                            subtitle: Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis),
                            trailing: Text(_formatDuration(song.durationMillis), style: TextStyle(color: Colors.grey)),
                            onTap: () => _mediaController.playSong(song),
                          );
                        },
                      ),
                    ),
                  ],
                );
              },
            ),
          ),
          // Mini Player
          StreamBuilder<PlayerState>(
            stream: _mediaController.subject,
            builder: (context, snapshot) {
              final state = snapshot.data;
              if (state == null || state.title.isEmpty) return SizedBox.shrink();

              return Container(
                color: Colors.grey[200],
                padding: EdgeInsets.all(8),
                child: Row(
                  children: [
                    Icon(Icons.music_note),
                    SizedBox(width: 8),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(state.title, maxLines: 1, overflow: TextOverflow.ellipsis, style: TextStyle(fontWeight: FontWeight.bold)),
                          Text(state.artist, maxLines: 1, overflow: TextOverflow.ellipsis, style: TextStyle(fontSize: 12)),
                        ],
                      ),
                    ),
                    IconButton(
                      icon: Icon(state.isPlaying ? Icons.pause : Icons.play_arrow),
                      onPressed: () {
                        _mediaController.playOrPause();
                      },
                    ),
                  ],
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}
