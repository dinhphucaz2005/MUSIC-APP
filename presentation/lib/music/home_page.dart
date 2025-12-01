import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:music/local_song_repository.dart';
import 'package:music/media_controller_manager.dart';
import 'package:music/song.dart';
import 'pages/playlists_page.dart';
import 'pages/songs_page.dart';
import 'widgets/full_player.dart';
import 'widgets/mini_player.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

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

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 400),
    );
    _animation = CurvedAnimation(
      parent: _animationController,
      curve: Curves.easeInOutCubicEmphasized,
      reverseCurve: Curves.easeInOutCubicEmphasized,
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  void _expandPlayer() {
    _animationController.forward();
  }

  void _collapsePlayer() {
    _animationController.reverse();
  }

  void _onVerticalDragUpdate(DragUpdateDetails details, double screenHeight) {
    if (!_isDragging) {
      setState(() => _isDragging = true);
    }

    setState(() {
      _dragOffset -= details.primaryDelta! / screenHeight;
      _dragOffset = _dragOffset.clamp(0.0, 1.0);
      _animationController.value = _dragOffset;
    });
  }

  void _onVerticalDragEnd(DragEndDetails details, double screenHeight) {
    setState(() => _isDragging = false);

    final velocity = details.primaryVelocity ?? 0;
    final shouldExpand = velocity < -300 || (_dragOffset > 0.5 && velocity >= -300);

    if (shouldExpand) {
      _expandPlayer();
    } else {
      _collapsePlayer();
    }
  }

  @override
  Widget build(BuildContext context) {
    final pages = [
      SongsPage(repository: _repository, mediaController: _mediaController),
      const PlaylistsPage(),
    ];
    final screenHeight = MediaQuery.of(context).size.height;

    return StreamBuilder<PlayerState>(
      stream: _mediaController.subject,
      builder: (context, snapshot) {
        final playerState = snapshot.data;
        final hasPlayer = playerState != null && playerState.currentSong != null;
        final song = playerState?.currentSong;

        return Scaffold(
          body: Stack(
            children: [
              // Main content
              Column(
                children: [
                  Expanded(child: pages[_currentIndex]),
                  if (hasPlayer) const SizedBox(height: 72), // Space for mini player
                ],
              ),

              // Draggable Player (Mini to Full transition)
              if (hasPlayer)
                AnimatedBuilder(
                  animation: _animation,
                  builder: (context, child) {
                    final progress = _animation.value;
                    final isFullyCollapsed = progress == 0;

                    return Stack(
                      children: [
                        // Background blur and overlay (full screen)
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
                          bottom: 0,
                          height: isFullyCollapsed ? 72 : screenHeight,
                          child: GestureDetector(
                            onVerticalDragUpdate: (details) => _onVerticalDragUpdate(details, screenHeight),
                            onVerticalDragEnd: (details) => _onVerticalDragEnd(details, screenHeight),
                            onTap: isFullyCollapsed ? _expandPlayer : null,
                            child: Container(
                              decoration: BoxDecoration(
                                gradient: LinearGradient(
                                  begin: Alignment.topLeft,
                                  end: Alignment.bottomRight,
                                  colors: [
                                    Theme.of(context).cardColor,
                                    Theme.of(context).primaryColor.withValues(alpha: 0.05),
                                  ],
                                ),
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
                                    // Mini Player (visible when collapsed)
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

                                    // Full Player (visible when expanded)
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

                                    // Drag indicator (visible when expanded)
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
                                                color: Colors.grey[400],
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
          bottomNavigationBar: BottomNavigationBar(
            currentIndex: _currentIndex,
            type: BottomNavigationBarType.fixed,
            backgroundColor: hasPlayer ? Colors.transparent : null,
            elevation: hasPlayer ? 0 : 8,
            selectedItemColor: Theme.of(context).primaryColor,
            unselectedItemColor: Colors.grey,
            onTap: (index) {
              setState(() {
                _currentIndex = index;
              });
            },
            items: const [
              BottomNavigationBarItem(icon: Icon(Icons.music_note), label: 'Songs'),
              BottomNavigationBarItem(icon: Icon(Icons.playlist_play), label: 'Playlists'),
            ],
          ),
        );
      },
    );
  }
}
