import 'package:shadcn_flutter/shadcn_flutter.dart';

/// App theme configuration for shadcn_flutter
class AppTheme {
  AppTheme._();

  /// Primary accent color for the music app
  static const Color primaryColor = Color(0xFF6366F1); // Indigo
  static const Color secondaryColor = Color(0xFF8B5CF6); // Violet
  static const Color accentColor = Color(0xFFF472B6); // Pink

  /// Dark theme configuration
  static ThemeData darkTheme() {
    return ThemeData(
      colorScheme: const ColorScheme(
        brightness: Brightness.dark,
        background: Color(0xFF09090B),
        foreground: Color(0xFFFAFAFA),
        card: Color(0xFF18181B),
        cardForeground: Color(0xFFFAFAFA),
        popover: Color(0xFF18181B),
        popoverForeground: Color(0xFFFAFAFA),
        primary: Color(0xFF6366F1),
        primaryForeground: Color(0xFFFAFAFA),
        secondary: Color(0xFF27272A),
        secondaryForeground: Color(0xFFFAFAFA),
        muted: Color(0xFF27272A),
        mutedForeground: Color(0xFFA1A1AA),
        accent: Color(0xFF27272A),
        accentForeground: Color(0xFFFAFAFA),
        destructive: Color(0xFFEF4444),
        destructiveForeground: Color(0xFFFAFAFA),
        border: Color(0xFF27272A),
        input: Color(0xFF27272A),
        ring: Color(0xFF6366F1),
        chart1: Color(0xFF6366F1),
        chart2: Color(0xFF8B5CF6),
        chart3: Color(0xFFF472B6),
        chart4: Color(0xFF22D3EE),
        chart5: Color(0xFF22C55E),
        // Sidebar colors
        sidebar: Color(0xFF18181B),
        sidebarForeground: Color(0xFFFAFAFA),
        sidebarPrimary: Color(0xFF6366F1),
        sidebarPrimaryForeground: Color(0xFFFAFAFA),
        sidebarAccent: Color(0xFF27272A),
        sidebarAccentForeground: Color(0xFFFAFAFA),
        sidebarBorder: Color(0xFF27272A),
        sidebarRing: Color(0xFF6366F1),
      ),
      radius: 0.75,
    );
  }

  /// Light theme configuration
  static ThemeData lightTheme() {
    return ThemeData(
      colorScheme: const ColorScheme(
        brightness: Brightness.light,
        background: Color(0xFFFFFFFF),
        foreground: Color(0xFF09090B),
        card: Color(0xFFFFFFFF),
        cardForeground: Color(0xFF09090B),
        popover: Color(0xFFFFFFFF),
        popoverForeground: Color(0xFF09090B),
        primary: Color(0xFF6366F1),
        primaryForeground: Color(0xFFFAFAFA),
        secondary: Color(0xFFF4F4F5),
        secondaryForeground: Color(0xFF18181B),
        muted: Color(0xFFF4F4F5),
        mutedForeground: Color(0xFF71717A),
        accent: Color(0xFFF4F4F5),
        accentForeground: Color(0xFF18181B),
        destructive: Color(0xFFEF4444),
        destructiveForeground: Color(0xFFFAFAFA),
        border: Color(0xFFE4E4E7),
        input: Color(0xFFE4E4E7),
        ring: Color(0xFF6366F1),
        chart1: Color(0xFF6366F1),
        chart2: Color(0xFF8B5CF6),
        chart3: Color(0xFFF472B6),
        chart4: Color(0xFF22D3EE),
        chart5: Color(0xFF22C55E),
        // Sidebar colors
        sidebar: Color(0xFFF4F4F5),
        sidebarForeground: Color(0xFF09090B),
        sidebarPrimary: Color(0xFF6366F1),
        sidebarPrimaryForeground: Color(0xFFFAFAFA),
        sidebarAccent: Color(0xFFE4E4E7),
        sidebarAccentForeground: Color(0xFF18181B),
        sidebarBorder: Color(0xFFE4E4E7),
        sidebarRing: Color(0xFF6366F1),
      ),
      radius: 0.75,
    );
  }

  /// Custom gradients for music app
  static const LinearGradient primaryGradient = LinearGradient(
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
    colors: [primaryColor, secondaryColor],
  );

  static const LinearGradient playerGradient = LinearGradient(
    begin: Alignment.topCenter,
    end: Alignment.bottomCenter,
    colors: [
      Color(0xFF1F1F23),
      Color(0xFF0F0F12),
    ],
  );

  /// Glass morphism decoration
  static BoxDecoration glassDecoration({double opacity = 0.1}) {
    return BoxDecoration(
      color: Colors.white.withValues(alpha: opacity),
      borderRadius: BorderRadius.circular(16),
      border: Border.all(
        color: Colors.white.withValues(alpha: 0.2),
        width: 1,
      ),
    );
  }
}

/// Extension for easy access to theme colors
extension ThemeContextExtension on BuildContext {
  ThemeData get theme => Theme.of(this);

  ColorScheme get colorScheme => Theme.of(this).colorScheme;

  Color get primaryColor => colorScheme.primary;

  Color get backgroundColor => colorScheme.background;

  Color get surfaceColor => colorScheme.card;

  Color get textColor => colorScheme.foreground;

  Color get mutedColor => colorScheme.mutedForeground;
}
