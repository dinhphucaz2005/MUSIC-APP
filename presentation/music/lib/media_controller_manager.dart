import 'package:music/local_song.dart';
import 'package:music/music_platform_interface.dart';
import 'package:rxdart/rxdart.dart';

class PlayerState {
  final bool isPlaying;
  final String title;
  final String artist;
  final int duration;
  final int position;

  PlayerState({this.isPlaying = false, this.title = '', this.artist = '', this.duration = 0, this.position = 0});
}

class MediaControllerManager {
  final subject = BehaviorSubject<PlayerState>.seeded(PlayerState());
  final _platform = MusicPlatform.instance;

  MediaControllerManager() {
    _platform.playerStateStream.listen((event) {
      if (event is Map) {
        final state = PlayerState(
          isPlaying: event['isPlaying'] as bool? ?? false,
          title: event['title'] as String? ?? '',
          artist: event['artist'] as String? ?? '',
          duration: (event['duration'] as num?)?.toInt() ?? 0,
          position: (event['position'] as num?)?.toInt() ?? 0,
        );
        subject.add(state);
      }
    });
  }

  Future<void> playOrPause() async {
    _platform.playOrPause();
  }

  Future<void> playSong(LocalSong song) async {
    _platform.playSong(song.path);
  }
}
