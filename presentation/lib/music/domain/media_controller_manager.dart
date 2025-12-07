import 'package:presentation/music/domain/music_method_channel.dart';
import 'package:presentation/music/domain/song.dart';
import 'package:rxdart/rxdart.dart';

class PlayerState {
  final bool? isPlaying;
  final Song? currentSong;

  PlayerState({this.isPlaying, this.currentSong});
}

class MediaControllerManager {
  final subject = BehaviorSubject<PlayerState>.seeded(PlayerState());
  final _platform = MethodChannelMusic();
  Song? _currentSong;

  MediaControllerManager() {
    _platform.playerStateStream.listen((event) {
      if (event is Map) {
        final isPlaying = event['isPlaying'] as bool? ?? false;
        final title = event['title'] as String? ?? '';

        if (title.isNotEmpty && _currentSong != null) {
          subject.add(PlayerState(isPlaying: isPlaying, currentSong: _currentSong));
        }
      }
    });
  }

  Future<void> playOrPause() async {
    _platform.playOrPause();
  }

  Future<void> playSong(LocalSong song) async {
    _currentSong = song;
    _platform.playSong(song.path);
    subject.add(PlayerState(isPlaying: true, currentSong: song));
  }
}
