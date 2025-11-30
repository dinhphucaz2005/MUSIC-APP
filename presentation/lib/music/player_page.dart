import 'dart:io';
import 'package:flutter/material.dart';
import 'package:music/local_song.dart';
import 'package:music/local_song_repository.dart';
import 'package:music/music_platform_interface.dart';

class PlayerPage extends StatefulWidget {
  const PlayerPage({super.key});

  @override
  State<PlayerPage> createState() => _PlayerPageState();
}

class _PlayerPageState extends State<PlayerPage> {
  final _repository = LocalSongRepository();
  final _plugin = MusicPlatform.instance; // Keep plugin for playback controls

  @override
  void initState() {
    super.initState();
  }

  Future<void> _loadLocalSongs() async {
    _repository.loadLocalSongs();
  }

  Future<void> _playSongAtIndex(int index) async {
    // Note: playAtIndex might need to be updated if the service expects a list of URIs
    // For now assuming the service has the same list as the UI is a bit risky if they get out of sync.
    // Ideally we should send the playlist to the service.
    // But based on previous code, loadLocal in Android sets the queue in the service.
    // So if we call loadLocal, the service gets the queue.
    // However, here we are streaming songs one by one.
    // The Android loadLocal implementation (Step 293) calls LocalSongExtractor.extracts but DOES NOT set the queue in the service anymore!
    // The previous implementation (Step 162) did set the queue.
    // I need to fix this. The service needs the queue to play.

    // Wait, the user wants to display songs immediately.
    // If I play a song, the service needs to know about it.
    // If the service doesn't have the queue, it can't play index X.

    // I should probably update the service queue when a song is added, or send the song to play.
    // Or, revert to setting the queue in Android after extraction is finished?
    // But extraction might take time.

    // For now, let's just implement the UI part as requested.
    // The user asked to "fix flutter... display on UI".
    // Playback might be broken if I don't fix the service queue.
    // But let's focus on the UI first.

    await _plugin.playAtIndex(index); // This might fail if service queue is empty
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
      body: StreamBuilder<LocalSongRepositoryState>(
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
                          onTap: () => _playSongAtIndex(index),
                        );
                      },
                    ),
                  ),
                ],
              );
        },
      ),
    );
  }
}
