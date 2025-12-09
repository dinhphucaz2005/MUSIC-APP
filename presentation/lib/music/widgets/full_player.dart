import "dart:io";
import "dart:ui";
import "package:flutter/material.dart";
import "package:presentation/music/widgets/animated_equalizer.dart";

enum RepeatMode { off, one, all }

class FullPlayer extends StatefulWidget {
  const FullPlayer({
    required this.title,
    required this.artist,
    required this.isPlaying,
    required this.duration,
    required this.position,
    required this.onPlayPause,
    required this.onPrevious,
    required this.onNext,
    required this.onSeek,
    super.key,
    this.thumbnailPath,
    this.onClose,
    this.onShuffle,
    this.onRepeat,
    this.onQueue,
    this.onLyrics,
    this.isShuffleEnabled = false,
    this.repeatMode = RepeatMode.off,
  });
  final String title;
  final String artist;
  final String? thumbnailPath;
  final bool isPlaying;
  final int duration;
  final int position;
  final VoidCallback onPlayPause;
  final VoidCallback onPrevious;
  final VoidCallback onNext;
  final Function(double) onSeek;
  final VoidCallback? onClose;
  final VoidCallback? onShuffle;
  final VoidCallback? onRepeat;
  final VoidCallback? onQueue;
  final VoidCallback? onLyrics;
  final bool isShuffleEnabled;
  final RepeatMode repeatMode;

  @override
  State<FullPlayer> createState() => _FullPlayerState();
}

class _FullPlayerState extends State<FullPlayer> {
  Widget _buildThumbnail(ThemeData theme) {
    const size = 280.0;

    if (widget.thumbnailPath == null || widget.thumbnailPath!.isEmpty) {
      return Container(
        width: size,
        height: size,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Colors.white.withValues(alpha: 0.2),
              Colors.white.withValues(alpha: 0.1),
            ],
          ),
          borderRadius: BorderRadius.circular(24),
        ),
        child: Icon(
          Icons.music_note_rounded,
          color: Colors.white.withValues(alpha: 0.8),
          size: 80,
        ),
      );
    }

    if (widget.thumbnailPath!.startsWith("/")) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(24),
        child: Image.file(
          File(widget.thumbnailPath!),
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(theme, size),
        ),
      );
    }

    if (widget.thumbnailPath!.startsWith("http")) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(24),
        child: Image.network(
          widget.thumbnailPath!,
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(theme, size),
        ),
      );
    }

    return _buildPlaceholder(theme, size);
  }

  Widget _buildPlaceholder(ThemeData theme, double size) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Colors.white.withValues(alpha: 0.2),
            Colors.white.withValues(alpha: 0.1),
          ],
        ),
        borderRadius: BorderRadius.circular(24),
      ),
      child: Icon(
        Icons.broken_image_rounded,
        color: Colors.white.withValues(alpha: 0.6),
        size: 60,
      ),
    );
  }

  String _formatDuration(int milliseconds) {
    final seconds = milliseconds ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  IconData _getRepeatIcon() {
    switch (widget.repeatMode) {
      case RepeatMode.one:
        return Icons.repeat_one_rounded;
      case RepeatMode.all:
      case RepeatMode.off:
        return Icons.repeat_rounded;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Stack(
      children: [
        // Background blur effect
        if (widget.thumbnailPath != null && widget.thumbnailPath!.isNotEmpty)
          Positioned.fill(
            child: Image.file(
              File(widget.thumbnailPath!),
              fit: BoxFit.cover,
              errorBuilder: (_, __, ___) => Container(color: Colors.black),
            ),
          ),

        // Blur overlay
        Positioned.fill(
          child: BackdropFilter(
            filter: ImageFilter.blur(sigmaX: 50, sigmaY: 50),
            child: Container(
              color: Colors.black.withValues(alpha: 0.6),
            ),
          ),
        ),

        // Main content
        SafeArea(
          child: Column(
            children: [
              // Header
              Padding(
                padding:
                    const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    if (widget.onClose != null)
                      IconButton(
                        icon: const Icon(
                          Icons.keyboard_arrow_down_rounded,
                          color: Colors.white,
                          size: 28,
                        ),
                        onPressed: widget.onClose,
                      )
                    else
                      const SizedBox(width: 40),
                    Text(
                      "NOW PLAYING",
                      style: TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w600,
                        letterSpacing: 1.5,
                        color: Colors.white.withValues(alpha: 0.7),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(
                        Icons.more_horiz_rounded,
                        color: Colors.white,
                      ),
                      onPressed: widget.onQueue,
                    ),
                  ],
                ),
              ),

              const Spacer(flex: 1),

              // Album art với shadow đẹp
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 40),
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(24),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.5),
                        blurRadius: 30,
                        offset: const Offset(0, 15),
                      ),
                    ],
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(24),
                    child: _buildThumbnail(theme),
                  ),
                ),
              ),

              const Spacer(flex: 1),

              // Song info
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Column(
                  children: [
                    Text(
                      widget.title,
                      style: const TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                      textAlign: TextAlign.center,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      widget.artist,
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.white.withValues(alpha: 0.7),
                      ),
                      textAlign: TextAlign.center,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 32),

              // Progress bar
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Column(
                  children: [
                    SliderTheme(
                      data: SliderTheme.of(context).copyWith(
                        activeTrackColor: Colors.white,
                        inactiveTrackColor: Colors.white.withValues(alpha: 0.3),
                        thumbColor: Colors.white,
                        overlayColor: Colors.white.withValues(alpha: 0.2),
                        trackHeight: 4,
                        thumbShape:
                            const RoundSliderThumbShape(enabledThumbRadius: 8),
                      ),
                      child: Slider(
                        value: widget.duration > 0
                            ? (widget.position / widget.duration)
                                .clamp(0.0, 1.0)
                            : 0.0,
                        onChanged: (value) =>
                            widget.onSeek(value * widget.duration),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          _formatDuration(widget.position),
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.white.withValues(alpha: 0.7),
                          ),
                        ),
                        Text(
                          _formatDuration(widget.duration),
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.white.withValues(alpha: 0.7),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 32),

              // Control buttons
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    // Shuffle
                    IconButton(
                      onPressed: widget.onShuffle,
                      icon: Icon(
                        Icons.shuffle_rounded,
                        color: widget.isShuffleEnabled
                            ? Colors.white
                            : Colors.white.withValues(alpha: 0.5),
                        size: 24,
                      ),
                    ),

                    // Previous
                    IconButton(
                      onPressed: widget.onPrevious,
                      icon: const Icon(
                        Icons.skip_previous_rounded,
                        color: Colors.white,
                        size: 32,
                      ),
                    ),

                    // Play/Pause với animated equalizer
                    Container(
                      width: 80,
                      height: 80,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        shape: BoxShape.circle,
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.3),
                            blurRadius: 20,
                            offset: const Offset(0, 8),
                          ),
                        ],
                      ),
                      child: InkWell(
                        onTap: widget.onPlayPause,
                        borderRadius: BorderRadius.circular(40),
                        child: Center(
                          child: widget.isPlaying
                              ? const AnimatedEqualizer(
                                  color: Colors.black,
                                  size: 32,
                                  barCount: 4,
                                  speed: 1.2,
                                  spacing: 2,
                                )
                              : const Icon(
                                  Icons.play_arrow_rounded,
                                  color: Colors.black,
                                  size: 40,
                                ),
                        ),
                      ),
                    ),

                    // Next
                    IconButton(
                      onPressed: widget.onNext,
                      icon: const Icon(
                        Icons.skip_next_rounded,
                        color: Colors.white,
                        size: 32,
                      ),
                    ),

                    // Repeat
                    IconButton(
                      onPressed: widget.onRepeat,
                      icon: Icon(
                        _getRepeatIcon(),
                        color: widget.repeatMode != RepeatMode.off
                            ? Colors.white
                            : Colors.white.withValues(alpha: 0.5),
                        size: 24,
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 24),

              // Bottom actions
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    IconButton(
                      onPressed: () {
                        // TODO: Add to favorites
                      },
                      icon: Icon(
                        Icons.favorite_border_rounded,
                        color: Colors.white.withValues(alpha: 0.7),
                      ),
                    ),
                    IconButton(
                      onPressed: () {
                        // TODO: Show lyrics
                      },
                      icon: Icon(
                        Icons.lyrics_rounded,
                        color: Colors.white.withValues(alpha: 0.7),
                      ),
                    ),
                    IconButton(
                      onPressed: () {
                        // TODO: Share
                      },
                      icon: Icon(
                        Icons.share_rounded,
                        color: Colors.white.withValues(alpha: 0.7),
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 24),
            ],
          ),
        ),
      ],
    );
  }
}
