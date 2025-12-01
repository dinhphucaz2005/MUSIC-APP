import 'package:flutter/material.dart';

import 'music/home_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    MaterialApp(
      title: 'Music App',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.lightBlueAccent),
        useMaterial3: true,
        appBarTheme: const AppBarTheme(centerTitle: false, elevation: 0),
      ),
      debugShowCheckedModeBanner: false,
      home: const HomePage(),
    ),
  );
}
