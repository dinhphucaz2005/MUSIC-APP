import 'package:music/song.dart';
import 'package:music/music_platform_interface.dart';
import 'package:rxdart/rxdart.dart';

class LocalSongRepositoryState {
  final List<LocalSong> songs;
  final bool isLoading;

  LocalSongRepositoryState({this.songs = const [], this.isLoading = false});
}

class LocalSongRepository {
  final subject = BehaviorSubject<LocalSongRepositoryState>.seeded(
    LocalSongRepositoryState(),
  );
  final _platform = MusicPlatform.instance;

  LocalSongRepository() {
    _platform.songStream.listen((event) {
      if (event is Map) {
        final type = event['type'] as String?;
        final currentState = subject.value;

        switch (type) {
          case 'start':
            subject.add(LocalSongRepositoryState(songs: [], isLoading: true));
            break;
          case 'add':
            final songData = Map<String, dynamic>.from(event['song'] as Map);
            final song = LocalSong(
              path: songData['filePath'] as String? ?? '',
              title: songData['title'] as String? ?? 'Unknown',
              artist: songData['artist'] as String? ?? 'Unknown',
              thumbnailPath: songData['thumbnailPath'] as String? ?? '',
              durationMillis: (songData['duration'] as num?)?.toInt() ?? 0,
              status: LocalSongStatus.unchanged,
            );
            subject.add(
              LocalSongRepositoryState(
                songs: [...currentState.songs, song],
                isLoading: currentState.isLoading,
              ),
            );
            break;
          case 'remove':
            final id = event['id'] as String?;
            if (id != null) {
              final updatedSongs =
                  currentState.songs.where((s) => s.path != id).toList();
              subject.add(
                LocalSongRepositoryState(
                  songs: updatedSongs,
                  isLoading: currentState.isLoading,
                ),
              );
            }
            break;
          case 'finish':
            subject.add(
              LocalSongRepositoryState(
                songs: currentState.songs,
                isLoading: false,
              ),
            );
            break;
        }
      }
    });
  }

  void loadLocalSongs() {
    subject.add(
      LocalSongRepositoryState(songs: subject.value.songs, isLoading: true),
    );
    _platform.loadLocalSongs();
  }

  Future<void> playAtIndex(int index) async {
    await _platform.playAtIndex(index);
  }
}
