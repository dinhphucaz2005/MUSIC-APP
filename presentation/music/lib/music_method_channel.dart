import 'dart:async';

import 'package:flutter/services.dart';

import 'music_platform_interface.dart';

class MethodChannelMusic extends MusicPlatform {
  final methodChannel = const MethodChannel('music');
  final eventChannel = const EventChannel('music_events');
  final playerEventChannel = const EventChannel('music_player_events');

  final _songController = StreamController.broadcast();
  final _playerController = StreamController.broadcast();

  bool _songStreamStarted = false;
  bool _playerStreamStarted = false;

  // === REPLAY BUFFER ===
  final List<dynamic> _songBacklog = [];
  final List<dynamic> _playerBacklog = [];

  MethodChannelMusic() {
    _initSongStream();
    _initPlayerStream();
  }

  void _initSongStream() {
    if (_songStreamStarted) return;
    _songStreamStarted = true;

    eventChannel.receiveBroadcastStream().listen((event) {
      _songBacklog.add(event);
      if (_songBacklog.length > 200) {
        _songBacklog.removeAt(0);
      }

      _songController.add(event);
    });
  }

  void _initPlayerStream() {
    if (_playerStreamStarted) return;
    _playerStreamStarted = true;

    playerEventChannel.receiveBroadcastStream().listen((event) {
      _playerBacklog.add(event);
      if (_playerBacklog.length > 200) {
        _playerBacklog.removeAt(0);
      }

      _playerController.add(event);
    });
  }

  @override
  Stream<dynamic> get songStream {
    Future.microtask(() {
      for (var e in _songBacklog) {
        _songController.add(e);
      }
    });
    return _songController.stream;
  }

  @override
  Stream<dynamic> get playerStateStream {
    Future.microtask(() {
      for (var e in _playerBacklog) {
        _playerController.add(e);
      }
    });
    return _playerController.stream;
  }

  @override
  void loadLocalSongs() {
    methodChannel.invokeMethod('loadLocal');
  }

  @override
  Future<void> playAtIndex(int index) async {
    await methodChannel.invokeMethod('playAtIndex', {'index': index});
  }

  @override
  Future<void> playOrPause() async {
    await methodChannel.invokeMethod('playOrPause');
  }

  @override
  Future<void> playSong(String path) {
    return methodChannel.invokeMethod('playSong', {'path': path});
  }
}
