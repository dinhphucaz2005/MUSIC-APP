part of "songs_cubit.dart";

abstract class SongsState extends Equatable {
  const SongsState();

  @override
  List<Object> get props => [];
}

class SongsInitial extends SongsState {}

class SongsLoading extends SongsState {}

class SongsLoaded extends SongsState {
  const SongsLoaded(this.songs);
  final List<LocalSong> songs;

  @override
  List<Object> get props => [songs];
}

class SongsError extends SongsState {
  const SongsError(this.message);
  final String message;

  @override
  List<Object> get props => [message];
}
