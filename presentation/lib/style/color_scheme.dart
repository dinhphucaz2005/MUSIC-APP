import 'package:flutter/material.dart';

extension BuildContextExt on BuildContext {
  ThemeData get colors => Theme.of(this);

  TextTheme get textStyles => Theme.of(this).textTheme;
}
