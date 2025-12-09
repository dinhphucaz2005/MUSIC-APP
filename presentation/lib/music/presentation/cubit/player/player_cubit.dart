import "package:equatable/equatable.dart";
import "package:flutter_bloc/flutter_bloc.dart";
import "package:injectable/injectable.dart";
import "package:music/media_controller_manager.dart" as manager;
import "package:music/song.dart";

part "player_state.dart";

@injectable
class PlayerCubit extends Cubit<PlayerState> {
  PlayerCubit(this._mediaController) : super(const PlayerState()) {
    _mediaController.subject.listen((state) {
      emit(
        PlayerState(
          isPlaying: state.isPlaying ?? false,
          currentSong: state.currentSong,
        ),
      );
    });
  }
  final manager.MediaControllerManager _mediaController;

  void playOrPause() => _mediaController.playOrPause();

  void playSong(LocalSong song) {
    _mediaController.playSong(song);
  }
}
