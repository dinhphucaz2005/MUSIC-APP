import 'package:flutter/services.dart';

import 'music_platform_interface.dart';

class MethodChannelMusic extends MusicPlatform {
  final methodChannel = const MethodChannel('music');
  final eventChannel = const EventChannel('music_events');
  final playerEventChannel = const EventChannel('music_player_events');

  @override
  void loadLocalSongs() {
    methodChannel.invokeMethod('loadLocal');
  }

  @override
  Future<void> playAtIndex(int index) async {
    await methodChannel.invokeMethod('playAtIndex', {'index': index});
  }

  @override
  Stream<dynamic> get songStream => eventChannel.receiveBroadcastStream();

  @override
  Stream<dynamic> get playerStateStream => playerEventChannel.receiveBroadcastStream();

  @override
  Future<void> playOrPause() async {
    await methodChannel.invokeMethod('playOrPause');
  }

  @override
  Future<void> playSong(String path) {
    return methodChannel.invokeMethod('playSong', {'path': path});
  }
}
