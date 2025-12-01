import 'dart:io';
import 'package:flutter/material.dart';

class MiniPlayer extends StatelessWidget {
  final String title;
  final String artist;
  final String? thumbnailPath;
  final bool isPlaying;
  final VoidCallback onPlayPause;
  final VoidCallback onTap;

  const MiniPlayer({
    super.key,
    required this.title,
    required this.artist,
    this.thumbnailPath,
    required this.isPlaying,
    required this.onPlayPause,
    required this.onTap,
  });

  Widget _buildThumbnail() {
    if (thumbnailPath == null || thumbnailPath!.isEmpty) {
      return Container(
        width: 48,
        height: 48,
        decoration: BoxDecoration(
          color: Colors.grey[300],
          borderRadius: BorderRadius.circular(6),
        ),
        child: Icon(Icons.music_note, color: Colors.grey[600], size: 24),
      );
    }

    if (thumbnailPath!.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(6),
        child: Image.file(
          File(thumbnailPath!),
          width: 48,
          height: 48,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => Container(
            width: 48,
            height: 48,
            decoration: BoxDecoration(
              color: Colors.grey[300],
              borderRadius: BorderRadius.circular(6),
            ),
            child: Icon(Icons.broken_image, color: Colors.grey[600]),
          ),
        ),
      );
    }

    if (thumbnailPath!.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(6),
        child: Image.network(
          thumbnailPath!,
          width: 48,
          height: 48,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => Container(
            width: 48,
            height: 48,
            decoration: BoxDecoration(
              color: Colors.grey[300],
              borderRadius: BorderRadius.circular(6),
            ),
            child: Icon(Icons.broken_image, color: Colors.grey[600]),
          ),
        ),
      );
    }

    return Container(
      width: 48,
      height: 48,
      decoration: BoxDecoration(
        color: Colors.grey[300],
        borderRadius: BorderRadius.circular(6),
      ),
      child: Icon(Icons.error, color: Colors.red[400]),
    );
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 72,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Theme.of(context).cardColor,
              Theme.of(context).primaryColor.withValues(alpha: 0.05),
            ],
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.15),
              blurRadius: 15,
              offset: const Offset(0, -3),
            ),
          ],
          border: Border(
            top: BorderSide(
              color: Theme.of(context).primaryColor.withValues(alpha: 0.1),
              width: 1,
            ),
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
          child: Row(
            children: [
              // Thumbnail with animation
              Hero(
                tag: 'album_art',
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.2),
                        blurRadius: 8,
                        offset: const Offset(0, 2),
                      ),
                    ],
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(8),
                    child: _buildThumbnail(),
                  ),
                ),
              ),
              const SizedBox(width: 14),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      title,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        fontWeight: FontWeight.w600,
                        fontSize: 15,
                        letterSpacing: 0.2,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      artist,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontSize: 13,
                        color: Colors.grey[600],
                        fontWeight: FontWeight.w400,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 8),
              // Play/Pause button with ripple effect
              Material(
                color: Colors.transparent,
                child: InkWell(
                  onTap: onPlayPause,
                  customBorder: const CircleBorder(),
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    child: Icon(
                      isPlaying ? Icons.pause_circle_filled : Icons.play_circle_filled,
                      size: 44,
                      color: Theme.of(context).primaryColor,
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 4),
            ],
          ),
        ),
      ),
    );
  }
}

