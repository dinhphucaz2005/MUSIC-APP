import 'dart:io';
import 'package:presentation/music/domain/song.dart';
import 'package:shadcn_flutter/shadcn_flutter.dart';
import 'animated_equalizer.dart';

class SongItem extends StatelessWidget {
  final LocalSong song;
  final VoidCallback onTap;
  final bool isPlaying;
  final bool isFavorite;
  final VoidCallback? onFavoriteToggle;

  const SongItem({
    super.key,
    required this.song,
    required this.onTap,
    this.isPlaying = false,
    this.isFavorite = false,
    this.onFavoriteToggle,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Card(
      child: GestureDetector(
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeInOut,
          decoration: BoxDecoration(
            border: isPlaying ? Border.all(color: theme.colorScheme.primary.withValues(alpha: 0.3), width: 1.5) : null,
            borderRadius: BorderRadius.circular(12),
          ),
          padding: const EdgeInsets.all(12),
          child: Row(
            children: [
              // Thumbnail
              Stack(
                alignment: Alignment.center,
                children: [
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(10),
                      boxShadow: isPlaying
                          ? [
                              BoxShadow(
                                color: theme.colorScheme.primary.withValues(alpha: 0.3),
                                blurRadius: 12,
                                offset: const Offset(0, 4),
                              ),
                            ]
                          : null,
                    ),
                    child: _buildThumbnail(song.thumbnailPath, theme),
                  ),
                  if (isPlaying)
                    Container(
                      width: 52,
                      height: 52,
                      decoration: BoxDecoration(
                        color: Colors.black.withValues(alpha: 0.5),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: const Center(
                        child: AnimatedEqualizer(
                          color: Colors.white,
                          size: 28,
                          barCount: 5,
                        ),
                      ),
                    ),
                ],
              ),
              const SizedBox(width: 14),

              // Song info
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      song.title,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontWeight: isPlaying ? FontWeight.bold : FontWeight.w600,
                        fontSize: 15,
                        color: isPlaying ? theme.colorScheme.primary : theme.colorScheme.foreground,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      song.artist,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontSize: 13,
                        color: isPlaying ? theme.colorScheme.primary.withValues(alpha: 0.8) : theme.colorScheme.mutedForeground,
                      ),
                    ),
                  ],
                ),
              ),

              // Duration badge
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: isPlaying ? theme.colorScheme.primary.withValues(alpha: 0.1) : theme.colorScheme.muted,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  _formatDuration(song.durationMillis),
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w500,
                    color: isPlaying ? theme.colorScheme.primary : theme.colorScheme.mutedForeground,
                  ),
                ),
              ),

              const SizedBox(width: 8),

              // Favorite button
              if (onFavoriteToggle != null)
                IconButton.ghost(
                  icon: Icon(
                    isFavorite ? Icons.favorite_rounded : Icons.favorite_border_rounded,
                    color: isFavorite ? Colors.red : theme.colorScheme.mutedForeground,
                    size: 20,
                  ),
                  density: ButtonDensity.compact,
                  onPressed: onFavoriteToggle,
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildThumbnail(String? thumbnailPath, ThemeData theme) {
    const size = 52.0;

    if (thumbnailPath == null || thumbnailPath.isEmpty) {
      return _buildPlaceholder(theme);
    }

    if (thumbnailPath.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(10),
        child: Image.file(
          File(thumbnailPath),
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(theme),
        ),
      );
    }

    if (thumbnailPath.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(10),
        child: Image.network(
          thumbnailPath,
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(theme),
        ),
      );
    }

    return _buildPlaceholder(theme);
  }

  Widget _buildPlaceholder(ThemeData theme) {
    return Container(
      width: 52,
      height: 52,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            theme.colorScheme.muted,
            theme.colorScheme.muted.withValues(alpha: 0.7),
          ],
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Icon(
        Icons.music_note_rounded,
        color: theme.colorScheme.mutedForeground,
        size: 24,
      ),
    );
  }

  String _formatDuration(int durationMillis) {
    final seconds = durationMillis ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }
}
