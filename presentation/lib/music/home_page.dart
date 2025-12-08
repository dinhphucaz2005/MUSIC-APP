import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:music/local_song_repository.dart';
import 'package:music/media_controller_manager.dart';
import 'package:music/song.dart';
import 'pages/playlists_page.dart';
import 'pages/songs_page.dart';
import 'pages/artists_page.dart';
import 'pages/favorites_page.dart';
import 'widgets/full_player.dart';
import 'widgets/mini_player.dart';

class HomePage extends StatefulWidget {
  final VoidCallback? onToggleTheme;

  const HomePage({super.key, this.onToggleTheme});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with SingleTickerProviderStateMixin {
  final _repository = LocalSongRepository();
  final _mediaController = MediaControllerManager();
  int _currentIndex = 0;
  late AnimationController _animationController;
  late Animation<double> _animation;
  double _dragOffset = 0.0;
  bool _isDragging = false;
  double _lastDragValue = 0.0;

  final List<_NavItem> _navItems = [
    _NavItem(icon: Icons.music_note_rounded, label: 'Songs'),
    _NavItem(icon: Icons.person_rounded, label: 'Artists'),
    _NavItem(icon: Icons.favorite_rounded, label: 'Favorites'),
    _NavItem(icon: Icons.playlist_play_rounded, label: 'Playlists'),
  ];

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 350),
    );
    _animation = CurvedAnimation(
      parent: _animationController,
      curve: Curves.easeInOutCubic,
      reverseCurve: Curves.easeInOutCubic,
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  void _expandPlayer() {
    _animationController.animateTo(1.0,
      duration: const Duration(milliseconds: 300),
      curve: Curves.easeOutCubic,
    );
  }

  void _collapsePlayer() {
    _animationController.animateTo(0.0,
      duration: const Duration(milliseconds: 250),
      curve: Curves.easeInCubic,
    );
  }

  void _onVerticalDragUpdate(DragUpdateDetails details, double screenHeight) {
    if (!_isDragging) {
      setState(() {
        _isDragging = true;
        _lastDragValue = _animationController.value;
      });
    }

    // Cải thiện sensitivity và smoothness
    final delta = -details.primaryDelta! / (screenHeight * 0.8);
    final newValue = (_lastDragValue + delta).clamp(0.0, 1.0);

    _animationController.value = newValue;
    setState(() {
      _dragOffset = newValue;
    });
  }

  void _onVerticalDragEnd(DragEndDetails details, double screenHeight) {
    setState(() => _isDragging = false);

    final velocity = details.primaryVelocity ?? 0;
    final normalizedVelocity = velocity / screenHeight;

    // Improved threshold logic với velocity consideration
    final shouldExpand = normalizedVelocity < -0.3 ||
                        (_dragOffset > 0.4 && normalizedVelocity >= -0.3);

    if (shouldExpand) {
      _animationController.animateTo(1.0,
        duration: const Duration(milliseconds: 250),
        curve: Curves.easeOutCubic,
      );
    } else {
      _animationController.animateTo(0.0,
        duration: const Duration(milliseconds: 200),
        curve: Curves.easeInCubic,
      );
    }
  }

  Widget _buildPage(int index) {
    switch (index) {
      case 0:
        return SongsPage(repository: _repository, mediaController: _mediaController);
      case 1:
        return ArtistsPage(repository: _repository, mediaController: _mediaController);
      case 2:
        return FavoritesPage(repository: _repository, mediaController: _mediaController);
      case 3:
        return PlaylistsPage(repository: _repository, mediaController: _mediaController);
      default:
        return SongsPage(repository: _repository, mediaController: _mediaController);
    }
  }

  @override
  Widget build(BuildContext context) {
    final screenHeight = MediaQuery.of(context).size.height;
    final theme = Theme.of(context);

    return StreamBuilder<PlayerState>(
      stream: _mediaController.subject,
      builder: (context, snapshot) {
        final playerState = snapshot.data;
        final hasPlayer = playerState != null && playerState.currentSong != null;
        final song = playerState?.currentSong;

        return Scaffold(
          body: Stack(
            children: [
              // Main content with bottom navigation
              Column(
                children: [
                  // Page content
                  Expanded(child: _buildPage(_currentIndex)),

                  // Space for mini player if needed
                  if (hasPlayer) const SizedBox(height: 72),

                  // Bottom Navigation Bar
                  Container(
                    decoration: BoxDecoration(
                      color: theme.colorScheme.surface,
                      border: Border(
                        top: BorderSide(color: theme.colorScheme.outline, width: 1),
                      ),
                    ),
                    child: SafeArea(
                      top: false,
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 8),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: List.generate(_navItems.length, (index) {
                            final item = _navItems[index];
                            final isSelected = index == _currentIndex;

                            return GestureDetector(
                              onTap: () => setState(() => _currentIndex = index),
                              child: Container(
                                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                                decoration: BoxDecoration(
                                  color: isSelected ? theme.colorScheme.primary.withValues(alpha: 0.1) : Colors.transparent,
                                  borderRadius: BorderRadius.circular(16),
                                ),
                                child: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Icon(
                                      item.icon,
                                      color: isSelected ? theme.colorScheme.primary : theme.colorScheme.onSurface.withValues(alpha: 0.6),
                                      size: 24,
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      item.label,
                                      style: TextStyle(
                                        fontSize: 11,
                                        fontWeight: isSelected ? FontWeight.w600 : FontWeight.w400,
                                        color: isSelected ? theme.colorScheme.primary : theme.colorScheme.onSurface.withValues(alpha: 0.6),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            );
                          }),
                        ),
                      ),
                    ),
                  ),
                ],
              ),

              // Draggable Player
              if (hasPlayer)
                AnimatedBuilder(
                  animation: _animation,
                  builder: (context, child) {
                    final progress = _animation.value;
                    final isFullyCollapsed = progress == 0;
                    final bottomNavHeight = 72.0 + MediaQuery.of(context).padding.bottom;

                    return Stack(
                      children: [
                        // Background overlay
                        if (progress > 0)
                          Positioned.fill(
                            child: GestureDetector(
                              onTap: progress > 0.9 ? _collapsePlayer : null,
                              child: IgnorePointer(
                                ignoring: progress < 0.1,
                                child: Container(
                                  color: Colors.black.withValues(alpha: 0.7 * progress),
                                  child: BackdropFilter(
                                    filter: ImageFilter.blur(
                                      sigmaX: 15 * progress,
                                      sigmaY: 15 * progress,
                                    ),
                                    child: Container(color: Colors.transparent),
                                  ),
                                ),
                              ),
                            ),
                          ),

                        // Player container
                        Positioned(
                          left: 0,
                          right: 0,
                          bottom: isFullyCollapsed ? bottomNavHeight : 0,
                          height: isFullyCollapsed ? 72 : screenHeight,
                          child: GestureDetector(
                            onVerticalDragUpdate: (details) => _onVerticalDragUpdate(details, screenHeight),
                            onVerticalDragEnd: (details) => _onVerticalDragEnd(details, screenHeight),
                            onTap: isFullyCollapsed ? _expandPlayer : null,
                            child: Container(
                              decoration: BoxDecoration(
                                color: theme.colorScheme.primary,
                                borderRadius: BorderRadius.vertical(
                                  top: Radius.circular(20 - 10 * progress),
                                ),
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.2 + 0.1 * progress),
                                    blurRadius: 15 + 10 * progress,
                                    offset: Offset(0, -3 - 2 * progress),
                                  ),
                                ],
                              ),
                              child: ClipRRect(
                                borderRadius: BorderRadius.vertical(
                                  top: Radius.circular(20 - 10 * progress),
                                ),
                                child: Stack(
                                  children: [
                                    // Mini Player
                                    if (progress < 1)
                                      Positioned(
                                        left: 0,
                                        right: 0,
                                        bottom: 0,
                                        height: 72,
                                        child: Opacity(
                                          opacity: (1 - progress).clamp(0.0, 1.0),
                                          child: IgnorePointer(
                                            ignoring: progress > 0.5,
                                            child: MiniPlayer(
                                              title: (song is LocalSong) ? song.title : '',
                                              artist: (song is LocalSong) ? song.artist : '',
                                              thumbnailPath: (song is LocalSong) ? song.thumbnailPath : null,
                                              isPlaying: playerState.isPlaying ?? false,
                                              onPlayPause: () => _mediaController.playOrPause(),
                                              onTap: _expandPlayer,
                                            ),
                                          ),
                                        ),
                                      ),

                                    // Full Player
                                    if (progress > 0)
                                      Positioned.fill(
                                        child: Opacity(
                                          opacity: progress.clamp(0.0, 1.0),
                                          child: IgnorePointer(
                                            ignoring: progress < 0.5,
                                            child: FullPlayer(
                                              title: (song is LocalSong) ? song.title : '',
                                              artist: (song is LocalSong) ? song.artist : '',
                                              thumbnailPath: (song is LocalSong) ? song.thumbnailPath : null,
                                              isPlaying: playerState.isPlaying ?? false,
                                              duration: (song is LocalSong) ? song.durationMillis : 0,
                                              position: 0,
                                              onPlayPause: () => _mediaController.playOrPause(),
                                              onPrevious: () {
                                                // TODO: Implement previous
                                              },
                                              onNext: () {
                                                // TODO: Implement next
                                              },
                                              onSeek: (value) {
                                                // TODO: Implement seek
                                              },
                                              onClose: _collapsePlayer,
                                            ),
                                          ),
                                        ),
                                      ),

                                    // Drag indicator
                                    if (progress > 0.3)
                                      Positioned(
                                        top: 12,
                                        left: 0,
                                        right: 0,
                                        child: Center(
                                          child: Opacity(
                                            opacity: progress,
                                            child: Container(
                                              width: 40,
                                              height: 4,
                                              decoration: BoxDecoration(
                                                color: theme.colorScheme.primary,
                                                borderRadius: BorderRadius.circular(2),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    );
                  },
                ),
            ],
          ),
        );
      },
    );
  }
}

class _NavItem {
  final IconData icon;
  final String label;

  _NavItem({required this.icon, required this.label});
}
