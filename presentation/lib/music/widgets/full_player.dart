import 'dart:io';
import 'package:shadcn_flutter/shadcn_flutter.dart';

enum RepeatMode { off, one, all }

class FullPlayer extends StatefulWidget {
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

  const FullPlayer({
    super.key,
    required this.title,
    required this.artist,
    this.thumbnailPath,
    required this.isPlaying,
    required this.duration,
    required this.position,
    required this.onPlayPause,
    required this.onPrevious,
    required this.onNext,
    required this.onSeek,
    this.onClose,
    this.onShuffle,
    this.onRepeat,
    this.onQueue,
    this.onLyrics,
    this.isShuffleEnabled = false,
    this.repeatMode = RepeatMode.off,
  });

  @override
  State<FullPlayer> createState() => _FullPlayerState();
}

class _FullPlayerState extends State<FullPlayer> {
  Widget _buildThumbnail(ThemeData theme) {
    const size = 300.0;
    
    if (widget.thumbnailPath == null || widget.thumbnailPath!.isEmpty) {
      return Container(
        width: size,
        height: size,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              theme.colorScheme.primary.withValues(alpha: 0.3),
              theme.colorScheme.primary.withValues(alpha: 0.6),
            ],
          ),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Icon(
          Icons.music_note_rounded,
          color: theme.colorScheme.primaryForeground.withValues(alpha: 0.8),
          size: 100,
        ),
      );
    }

    if (widget.thumbnailPath!.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: Image.file(
          File(widget.thumbnailPath!),
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(theme, size),
        ),
      );
    }

    if (widget.thumbnailPath!.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(20),
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
            theme.colorScheme.muted,
            theme.colorScheme.muted.withValues(alpha: 0.7),
          ],
        ),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Icon(
        Icons.broken_image_rounded,
        color: theme.colorScheme.mutedForeground,
        size: 80,
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
    
    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            theme.colorScheme.background,
            theme.colorScheme.card,
          ],
        ),
      ),
      child: SafeArea(
        child: Column(
          children: [
            // Header
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  if (widget.onClose != null)
                    IconButton.ghost(
                      icon: const Icon(Icons.keyboard_arrow_down_rounded, size: 28),
                      onPressed: widget.onClose,
                    )
                  else
                    const SizedBox(width: 40),
                  
                  Text(
                    'NOW PLAYING',
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      letterSpacing: 1.5,
                      color: theme.colorScheme.mutedForeground,
                    ),
                  ),
                  
                  IconButton.ghost(
                    icon: const Icon(Icons.queue_music_rounded),
                    onPressed: widget.onQueue,
                  ),
                ],
              ),
            ),
            
            const Spacer(flex: 1),
            
            // Album art with shadow
            Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(20),
                boxShadow: [
                  BoxShadow(
                    color: theme.colorScheme.primary.withValues(alpha: 0.2),
                    blurRadius: 40,
                    offset: const Offset(0, 20),
                  ),
                ],
              ),
              child: _buildThumbnail(theme),
            ),
            
            const Spacer(flex: 1),
            
            // Song info
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32),
              child: Column(
                children: [
                  Text(
                    widget.title,
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: theme.colorScheme.foreground,
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
                      color: theme.colorScheme.mutedForeground,
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
              padding: const EdgeInsets.symmetric(horizontal: 32),
              child: Column(
                children: [
                  Slider(
                    value: SliderValue.single(widget.position.toDouble().clamp(0, widget.duration.toDouble())),
                    min: 0,
                    max: widget.duration.toDouble() > 0 ? widget.duration.toDouble() : 1,
                    onChanged: (value) => widget.onSeek(value.value),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        _formatDuration(widget.position),
                        style: TextStyle(
                          fontSize: 13,
                          color: theme.colorScheme.mutedForeground,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                      Text(
                        _formatDuration(widget.duration),
                        style: TextStyle(
                          fontSize: 13,
                          color: theme.colorScheme.mutedForeground,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            
            const SizedBox(height: 24),
            
            // Controls
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  // Shuffle
                  IconButton.ghost(
                    icon: Icon(
                      Icons.shuffle_rounded,
                      color: widget.isShuffleEnabled 
                          ? theme.colorScheme.primary 
                          : theme.colorScheme.mutedForeground,
                    ),
                    onPressed: widget.onShuffle,
                  ),
                  
                  // Previous
                  IconButton.ghost(
                    icon: Icon(
                      Icons.skip_previous_rounded,
                      size: 36,
                      color: theme.colorScheme.foreground,
                    ),
                    onPressed: widget.onPrevious,
                  ),
                  
                  // Play/Pause
                  Container(
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          theme.colorScheme.primary,
                          theme.colorScheme.primary.withValues(alpha: 0.8),
                        ],
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: theme.colorScheme.primary.withValues(alpha: 0.4),
                          blurRadius: 20,
                          offset: const Offset(0, 8),
                        ),
                      ],
                    ),
                    child: IconButton.ghost(
                      icon: Icon(
                        widget.isPlaying ? Icons.pause_rounded : Icons.play_arrow_rounded,
                        color: theme.colorScheme.primaryForeground,
                        size: 36,
                      ),
                      onPressed: widget.onPlayPause,
                    ),
                  ),
                  
                  // Next
                  IconButton.ghost(
                    icon: Icon(
                      Icons.skip_next_rounded,
                      size: 36,
                      color: theme.colorScheme.foreground,
                    ),
                    onPressed: widget.onNext,
                  ),
                  
                  // Repeat
                  IconButton.ghost(
                    icon: Icon(
                      _getRepeatIcon(),
                      color: widget.repeatMode != RepeatMode.off 
                          ? theme.colorScheme.primary 
                          : theme.colorScheme.mutedForeground,
                    ),
                    onPressed: widget.onRepeat,
                  ),
                ],
              ),
            ),
            
            const SizedBox(height: 24),
            
            // Extra controls
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 48),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  IconButton.ghost(
                    icon: Icon(
                      Icons.lyrics_rounded,
                      color: theme.colorScheme.mutedForeground,
                    ),
                    onPressed: widget.onLyrics,
                  ),
                  IconButton.ghost(
                    icon: Icon(
                      Icons.favorite_border_rounded,
                      color: theme.colorScheme.mutedForeground,
                    ),
                    onPressed: () {
                      // TODO: Toggle favorite
                    },
                  ),
                  IconButton.ghost(
                    icon: Icon(
                      Icons.share_rounded,
                      color: theme.colorScheme.mutedForeground,
                    ),
                    onPressed: () {
                      // TODO: Share song
                    },
                  ),
                ],
              ),
            ),
            
            const Spacer(flex: 1),
          ],
        ),
      ),
    );
  }
}
