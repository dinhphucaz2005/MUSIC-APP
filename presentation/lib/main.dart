import 'package:flutter/material.dart';
import 'music/player_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    MaterialApp(
      builder: (context, child) => Scaffold(body: child),
      home: const PlayerPage(),
    ),
  );
}
