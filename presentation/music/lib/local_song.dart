typedef JsonObject = Map<String, dynamic>;

enum LocalSongStatus { deleted, modified, unchanged, unknown }

class LocalSong {
  final String path;
  final String title;
  final String artist;
  final String thumbnailPath;
  final int durationMillis;
  final LocalSongStatus status;

  JsonObject toJson() {
    return {'path': path, 'title': title, 'artist': artist, 'thumbnailPath': thumbnailPath, 'durationMillis': durationMillis};
  }

  factory LocalSong.fromJson(JsonObject json) {
    return LocalSong(
      path: json['path'] as String,
      title: json['title'] as String,
      artist: json['artist'] as String,
      thumbnailPath: json['thumbnailPath'] as String,
      durationMillis: json['durationMillis'] as int,
      status: LocalSongStatus.values.firstWhere((e) => e.name == (json['status'] as String?), orElse: () => LocalSongStatus.unknown),
    );
  }

  LocalSong({
    required this.path,
    required this.title,
    required this.artist,
    required this.thumbnailPath,
    required this.durationMillis,
    required this.status,
  });
}

