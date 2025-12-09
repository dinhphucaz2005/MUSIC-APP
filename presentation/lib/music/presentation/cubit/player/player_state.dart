part of "player_cubit.dart";

class PlayerState extends Equatable {
  const PlayerState({
    this.isPlaying = false,
    this.currentSong,
  });
  final bool isPlaying;
  final Song? currentSong;

  @override
  List<Object?> get props => [isPlaying, currentSong];
}
