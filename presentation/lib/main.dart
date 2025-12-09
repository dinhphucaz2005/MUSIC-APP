import "package:flutter/material.dart";
import "package:flutter_bloc/flutter_bloc.dart";
import "package:presentation/di/injection.dart";
import "package:presentation/music/presentation/cubit/theme/theme_cubit.dart";
import "package:presentation/style/app_theme.dart";

import "package:presentation/music/home_page.dart";

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  configureDependencies();
  runApp(const MusicApp());
}

class MusicApp extends StatelessWidget {
  const MusicApp({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (_) => getIt<ThemeCubit>(),
      child: BlocBuilder<ThemeCubit, ThemeMode>(
        builder: (context, themeMode) {
          return MaterialApp(
            title: "Music App",
            debugShowCheckedModeBanner: false,
            themeMode: themeMode,
            theme: AppTheme.lightTheme(),
            darkTheme: AppTheme.darkTheme(),
            home: const HomePage(),
          );
        },
      ),
    );
  }
}
