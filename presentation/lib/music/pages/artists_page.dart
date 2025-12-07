import 'package:music/local_song_repository.dart';
import 'package:music/media_controller_manager.dart';
import 'package:music/song.dart';
import 'package:presentation/music/widgets/widgets.dart';
import 'package:shadcn_flutter/shadcn_flutter.dart';

class ArtistsPage extends StatefulWidget {
  final LocalSongRepository repository;
  final MediaControllerManager mediaController;

  const ArtistsPage({
    super.key,
    required this.repository,
    required this.mediaController,
  });

  @override
  State<ArtistsPage> createState() => _ArtistsPageState();
}

class _ArtistsPageState extends State<ArtistsPage> {
  Map<String, List<LocalSong>> _artists = {};

  @override
  void initState() {
    super.initState();
    _groupSongsByArtist();
  }

  void _groupSongsByArtist() {
    widget.repository.subject.listen((state) {
      final songs = state.songs;
      final Map<String, List<LocalSong>> artists = {};

      for (final song in songs) {
        final artistName = song.artist.isNotEmpty ? song.artist : 'Unknown Artist';
        if (!artists.containsKey(artistName)) {
          artists[artistName] = [];
        }
        artists[artistName]!.add(song);
      }

      if (mounted) {
        setState(() => _artists = artists);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      headers: [
        AppBar(
          title: const Text('Artists'),
          trailing: [
            IconButton.ghost(
              icon: const Icon(Icons.search_rounded),
              onPressed: () {
                // TODO: Implement artist search
              },
            ),
          ],
        ),
      ],
      child: _artists.isEmpty
          ? const EmptyState(
              icon: Icons.person_rounded,
              title: 'No artists found',
              subtitle: 'Load songs to see artists',
            )
          : ListView.builder(
              padding: const EdgeInsets.symmetric(vertical: 8),
              itemCount: _artists.length,
              itemBuilder: (context, index) {
                final artistName = _artists.keys.elementAt(index);
                final songs = _artists[artistName]!;

                return ListTileCard(
                  leading: CircleAvatar(text: artistName),
                  title: artistName,
                  subtitle: '${songs.length} songs',
                  trailing: Icon(
                    Icons.chevron_right_rounded,
                    color: Theme.of(context).colorScheme.mutedForeground,
                  ),
                  onTap: () => _openArtistDetail(artistName, songs),
                );
              },
            ),
    );
  }

  void _openArtistDetail(String artistName, List<LocalSong> songs) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(artistName),
        content: SizedBox(
          width: 400,
          height: 300,
          child: ListView.builder(
            itemCount: songs.length,
            itemBuilder: (context, index) {
              final song = songs[index];
              return Padding(
                padding: const EdgeInsets.symmetric(vertical: 4),
                child: GestureDetector(
                  onTap: () {
                    widget.mediaController.playSong(song);
                    Navigator.of(context).pop();
                  },
                  child: Row(
                    children: [
                      Text(
                        '${index + 1}',
                        style: TextStyle(color: Theme.of(context).colorScheme.mutedForeground),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis),
                            Text(
                              _formatDuration(song.durationMillis),
                              style: TextStyle(
                                fontSize: 12,
                                color: Theme.of(context).colorScheme.mutedForeground,
                              ),
                              maxLines: 1,
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
        actions: [
          Button.outline(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Close'),
          ),
          Button.primary(
            onPressed: () {
              if (songs.isNotEmpty) {
                widget.mediaController.playSong(songs.first);
              }
              Navigator.of(context).pop();
            },
            child: const Text('Play All'),
          ),
        ],
      ),
    );
  }

  String _formatDuration(int durationMillis) {
    final seconds = durationMillis ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }
}
