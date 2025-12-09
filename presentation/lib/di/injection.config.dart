// GENERATED CODE - DO NOT MODIFY BY HAND
// dart format width=80

// **************************************************************************
// InjectableConfigGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:get_it/get_it.dart' as _i174;
import 'package:injectable/injectable.dart' as _i526;
import 'package:music/local_song_repository.dart' as _i840;
import 'package:music/media_controller_manager.dart' as _i395;

import '../music/di/music_module.dart' as _i709;
import '../music/presentation/cubit/player/player_cubit.dart' as _i1020;
import '../music/presentation/cubit/songs/songs_cubit.dart' as _i174;
import '../music/presentation/cubit/theme/theme_cubit.dart' as _i672;

extension GetItInjectableX on _i174.GetIt {
// initializes the registration of main-scope dependencies inside of GetIt
  _i174.GetIt init({
    String? environment,
    _i526.EnvironmentFilter? environmentFilter,
  }) {
    final gh = _i526.GetItHelper(
      this,
      environment,
      environmentFilter,
    );
    final musicModule = _$MusicModule();
    gh.factory<_i672.ThemeCubit>(() => _i672.ThemeCubit());
    gh.lazySingleton<_i840.LocalSongRepository>(
        () => musicModule.localSongRepository);
    gh.lazySingleton<_i395.MediaControllerManager>(
        () => musicModule.mediaControllerManager);
    gh.factory<_i1020.PlayerCubit>(
        () => _i1020.PlayerCubit(gh<_i395.MediaControllerManager>()));
    gh.factory<_i174.SongsCubit>(
        () => _i174.SongsCubit(repository: gh<_i840.LocalSongRepository>()));
    return this;
  }
}

class _$MusicModule extends _i709.MusicModule {}
