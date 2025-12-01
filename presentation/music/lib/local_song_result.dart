import 'package:music/song.dart';

sealed class LocalSongResult {
  const LocalSongResult();
}

class NewLocalSong extends LocalSongResult {
  final LocalSong song;

  NewLocalSong({required this.song});
}

class LocalSongRemoved extends LocalSongResult {
  final String filePath;

  LocalSongRemoved({required this.filePath});
}
