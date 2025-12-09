import "package:injectable/injectable.dart";
import "package:music/local_song_repository.dart";
import "package:music/media_controller_manager.dart";

@module
abstract class MusicModule {
  @lazySingleton
  LocalSongRepository get localSongRepository => LocalSongRepository();

  @lazySingleton
  MediaControllerManager get mediaControllerManager => MediaControllerManager();
}
