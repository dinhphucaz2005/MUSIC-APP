import 'package:shadcn_flutter/shadcn_flutter.dart';
import 'package:presentation/style/app_theme.dart';

import 'music/home_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MusicApp());
}

class MusicApp extends StatefulWidget {
  const MusicApp({super.key});

  @override
  State<MusicApp> createState() => _MusicAppState();
}

class _MusicAppState extends State<MusicApp> {
  ThemeMode _themeMode = ThemeMode.dark;

  void _toggleTheme() {
    setState(() {
      _themeMode = _themeMode == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
    });
  }

  @override
  Widget build(BuildContext context) {
    return ShadcnApp(
      title: 'Music App',
      debugShowCheckedModeBanner: false,
      themeMode: _themeMode,
      theme: AppTheme.lightTheme(),
      darkTheme: AppTheme.darkTheme(),
      home: HomePage(onToggleTheme: _toggleTheme),
    );
  }
}
