import "package:equatable/equatable.dart";
import "package:flutter_bloc/flutter_bloc.dart";
import "package:injectable/injectable.dart";
import "package:music/local_song_repository.dart";
import "package:music/song.dart";

part "songs_state.dart";

@injectable
class SongsCubit extends Cubit<SongsState> {
  SongsCubit({
    required this.repository,
  }) : super(SongsInitial()) {
    repository.subject.listen((state) {
      if (state.isLoading) {
        emit(SongsLoading());
      } else {
        emit(SongsLoaded(state.songs));
      }
    });
    loadSongs();
  }

  final LocalSongRepository repository;

  void loadSongs() {
    repository.loadLocalSongs();
  }
}
