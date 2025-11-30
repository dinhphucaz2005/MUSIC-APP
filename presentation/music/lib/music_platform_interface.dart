import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'music_method_channel.dart';

abstract class MusicPlatform extends PlatformInterface {
  /// Constructs a MusicPlatform.
  MusicPlatform() : super(token: _token);

  static final Object _token = Object();

  static MusicPlatform _instance = MethodChannelMusic();

  /// The default instance of [MusicPlatform] to use.
  ///
  /// Defaults to [MethodChannelMusic].
  static MusicPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MusicPlatform] when
  /// they register themselves.
  static set instance(MusicPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  void loadLocalSongs();

  Future<void> playAtIndex(int index);

  Stream<dynamic> get songStream;

  Stream<dynamic> get playerStateStream;

  Future<void> playOrPause();

  Future<void> playSong(String path);
}
