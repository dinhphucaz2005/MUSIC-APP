import 'package:shadcn_flutter/shadcn_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SettingsPage extends StatefulWidget {
  final VoidCallback? onToggleTheme;

  const SettingsPage({super.key, this.onToggleTheme});

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  bool _isDarkMode = true;
  String _audioQuality = 'High';
  bool _crossfadeEnabled = false;
  int _crossfadeDuration = 5;

  @override
  void initState() {
    super.initState();
    _loadSettings();
  }

  Future<void> _loadSettings() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      _isDarkMode = prefs.getBool('dark_mode') ?? true;
      _audioQuality = prefs.getString('audio_quality') ?? 'High';
      _crossfadeEnabled = prefs.getBool('crossfade_enabled') ?? false;
      _crossfadeDuration = prefs.getInt('crossfade_duration') ?? 5;
    });
  }

  Future<void> _saveSettings() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool('dark_mode', _isDarkMode);
    await prefs.setString('audio_quality', _audioQuality);
    await prefs.setBool('crossfade_enabled', _crossfadeEnabled);
    await prefs.setInt('crossfade_duration', _crossfadeDuration);
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    
    return Scaffold(
      headers: [
        AppBar(
          title: const Text('Settings'),
        ),
      ],
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Appearance Section
          _SectionHeader(title: 'Appearance'),
          Card(
            child: _SettingsTile(
              icon: Icons.dark_mode_rounded,
              title: 'Dark Mode',
              subtitle: 'Use dark theme',
              trailing: Switch(
                value: _isDarkMode,
                onChanged: (value) {
                  setState(() => _isDarkMode = value);
                  widget.onToggleTheme?.call();
                  _saveSettings();
                },
              ),
            ),
          ),
          
          const SizedBox(height: 24),
          
          // Playback Section
          _SectionHeader(title: 'Playback'),
          Card(
            child: Column(
              children: [
                _SettingsTile(
                  icon: Icons.high_quality_rounded,
                  title: 'Audio Quality',
                  subtitle: _audioQuality,
                  onTap: () => _showQualityPicker(),
                ),
                Divider(color: theme.colorScheme.border),
                _SettingsTile(
                  icon: Icons.swap_horiz_rounded,
                  title: 'Crossfade',
                  subtitle: _crossfadeEnabled ? '${_crossfadeDuration}s between songs' : 'Disabled',
                  trailing: Switch(
                    value: _crossfadeEnabled,
                    onChanged: (value) {
                      setState(() => _crossfadeEnabled = value);
                      _saveSettings();
                    },
                  ),
                ),
                if (_crossfadeEnabled) ...[
                  Divider(color: theme.colorScheme.border),
                  Padding(
                    padding: const EdgeInsets.all(16),
                    child: Row(
                      children: [
                        Text('Duration: ${_crossfadeDuration}s'),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Slider(
                            value: SliderValue.single(_crossfadeDuration.toDouble()),
                            min: 1,
                            max: 12,
                            divisions: 11,
                            onChanged: (value) {
                              setState(() => _crossfadeDuration = value.value.round());
                              _saveSettings();
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ],
            ),
          ),
          
          const SizedBox(height: 24),
          
          // Storage Section
          _SectionHeader(title: 'Storage'),
          Card(
            child: Column(
              children: [
                _SettingsTile(
                  icon: Icons.cached_rounded,
                  title: 'Clear Cache',
                  subtitle: 'Free up storage space',
                  onTap: () => _showClearCacheConfirmation(),
                ),
                Divider(color: theme.colorScheme.border),
                _SettingsTile(
                  icon: Icons.folder_rounded,
                  title: 'Music Folders',
                  subtitle: 'Manage scan locations',
                  onTap: () {
                    // TODO: Implement music folder management
                  },
                ),
              ],
            ),
          ),
          
          const SizedBox(height: 24),
          
          // About Section
          _SectionHeader(title: 'About'),
          Card(
            child: Column(
              children: [
                _SettingsTile(
                  icon: Icons.info_rounded,
                  title: 'Version',
                  subtitle: '1.0.0',
                ),
                Divider(color: theme.colorScheme.border),
                _SettingsTile(
                  icon: Icons.code_rounded,
                  title: 'View Source',
                  subtitle: 'GitHub Repository',
                  onTap: () {
                    // TODO: Open GitHub link
                  },
                ),
              ],
            ),
          ),
          
          const SizedBox(height: 100),
        ],
      ),
    );
  }

  void _showQualityPicker() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Audio Quality'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            for (final quality in ['Low', 'Medium', 'High', 'Lossless'])
              GestureDetector(
                onTap: () {
                  setState(() => _audioQuality = quality);
                  _saveSettings();
                  Navigator.pop(context);
                },
                child: Container(
                  padding: const EdgeInsets.symmetric(vertical: 12),
                  child: Row(
                    children: [
                      Container(
                        width: 20,
                        height: 20,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(
                            color: quality == _audioQuality 
                                ? Theme.of(context).colorScheme.primary 
                                : Theme.of(context).colorScheme.mutedForeground,
                            width: 2,
                          ),
                        ),
                        child: quality == _audioQuality
                            ? Center(
                                child: Container(
                                  width: 10,
                                  height: 10,
                                  decoration: BoxDecoration(
                                    shape: BoxShape.circle,
                                    color: Theme.of(context).colorScheme.primary,
                                  ),
                                ),
                              )
                            : null,
                      ),
                      const SizedBox(width: 12),
                      Text(quality),
                    ],
                  ),
                ),
              ),
          ],
        ),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
        ],
      ),
    );
  }

  void _showClearCacheConfirmation() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Clear Cache'),
        content: const Text('This will clear all cached images and temporary files. Continue?'),
        actions: [
          Button.outline(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          Button.destructive(
            onPressed: () {
              // TODO: Implement cache clearing
              Navigator.pop(context);
              showToast(
                context: context,
                builder: (context, overlay) => const SurfaceCard(
                  child: Basic(
                    title: Text('Cache cleared successfully'),
                  ),
                ),
              );
            },
            child: const Text('Clear'),
          ),
        ],
      ),
    );
  }
}

class _SectionHeader extends StatelessWidget {
  final String title;

  const _SectionHeader({required this.title});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    
    return Padding(
      padding: const EdgeInsets.only(left: 4, bottom: 8),
      child: Text(
        title,
        style: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: theme.colorScheme.primary,
        ),
      ),
    );
  }
}

class _SettingsTile extends StatelessWidget {
  final IconData icon;
  final String title;
  final String subtitle;
  final Widget? trailing;
  final VoidCallback? onTap;

  const _SettingsTile({
    required this.icon,
    required this.title,
    required this.subtitle,
    this.trailing,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    
    return GestureDetector(
      onTap: onTap,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: theme.colorScheme.primary.withValues(alpha: 0.1),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Icon(icon, color: theme.colorScheme.primary, size: 20),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    style: const TextStyle(fontWeight: FontWeight.w600),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    subtitle,
                    style: TextStyle(
                      fontSize: 13,
                      color: theme.colorScheme.mutedForeground,
                    ),
                  ),
                ],
              ),
            ),
            if (trailing != null) trailing!,
            if (onTap != null && trailing == null)
              Icon(
                Icons.chevron_right_rounded,
                color: theme.colorScheme.mutedForeground,
              ),
          ],
        ),
      ),
    );
  }
}
