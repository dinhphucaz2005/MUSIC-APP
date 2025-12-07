import 'package:flutter/services.dart';

class MethodChannelMusic {
  final methodChannel = const MethodChannel('music');
  final eventChannel = const EventChannel('music_events');
  final playerEventChannel = const EventChannel('music_player_events');

  void loadLocalSongs() {
    methodChannel.invokeMethod('loadLocal');
  }

  Future<void> playAtIndex(int index) async {
    await methodChannel.invokeMethod('playAtIndex', {'index': index});
  }

  Stream<dynamic> get songStream => eventChannel.receiveBroadcastStream();

  Stream<dynamic> get playerStateStream => playerEventChannel.receiveBroadcastStream();

  Future<void> playOrPause() async {
    await methodChannel.invokeMethod('playOrPause');
  }

  Future<void> playSong(String path) {
    return methodChannel.invokeMethod('playSong', {'path': path});
  }
}
