import 'dart:io';
import 'package:music/song.dart';
import 'package:flutter/material.dart';
import 'package:presentation/style/color_scheme.dart';
import 'animated_equalizer.dart';

class SongItem extends StatelessWidget {
  final LocalSong song;
  final VoidCallback onTap;
  final bool isPlaying;

  const SongItem({super.key, required this.song, required this.onTap, this.isPlaying = false});

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeInOut,
          decoration: BoxDecoration(
            color: isPlaying ? context.colors.primaryColor.withValues(alpha: 0.1) : Colors.transparent,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(color: isPlaying ? context.colors.primaryColor.withValues(alpha: 0.2) : Colors.transparent, width: 1),
          ),
          padding: const EdgeInsets.all(8),
          child: Row(
            children: [
              Stack(
                alignment: Alignment.center,
                children: [
                  Hero(
                    tag: 'song_thumb_${song.path}',
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(12),
                        boxShadow: [
                          if (isPlaying)
                            BoxShadow(color: context.colors.primaryColor.withValues(alpha: 0.3), blurRadius: 12, offset: const Offset(0, 4)),
                        ],
                      ),
                      child: _buildThumbnail(song.thumbnailPath),
                    ),
                  ),
                  if (isPlaying)
                    Container(
                      width: 56,
                      height: 56,
                      decoration: BoxDecoration(color: Colors.black.withValues(alpha: 0.5), borderRadius: BorderRadius.circular(12)),
                      child: const Center(child: AnimatedEqualizer(color: Colors.white, size: 32, barCount: 5)),
                    ),
                ],
              ),
              const SizedBox(width: 16),
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
                        fontSize: 16,
                        color: isPlaying ? context.colors.primaryColor : context.colors.secondaryHeaderColor,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      song.artist,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(fontSize: 14, color: isPlaying ? context.colors.primaryColor.withValues(alpha: 0.8) : Colors.grey[600]),
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 12),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: isPlaying ? context.colors.primaryColor.withValues(alpha: 0.1) : Colors.grey.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Text(
                  _formatDuration(song.durationMillis),
                  style: TextStyle(fontSize: 12, fontWeight: FontWeight.w500, color: isPlaying ? context.colors.primaryColor : Colors.grey[600]),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildThumbnail(String? thumbnailPath) {
    if (thumbnailPath == null || thumbnailPath.isEmpty) {
      return _buildPlaceholder();
    }

    if (thumbnailPath.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: Image.file(File(thumbnailPath), width: 56, height: 56, fit: BoxFit.cover, errorBuilder: (_, __, ___) => _buildPlaceholder()),
      );
    }

    if (thumbnailPath.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: Image.network(thumbnailPath, width: 56, height: 56, fit: BoxFit.cover, errorBuilder: (_, __, ___) => _buildPlaceholder()),
      );
    }

    return _buildPlaceholder();
  }

  Widget _buildPlaceholder() {
    return Container(
      width: 56,
      height: 56,
      decoration: BoxDecoration(
        gradient: LinearGradient(begin: Alignment.topLeft, end: Alignment.bottomRight, colors: [Colors.grey.shade200, Colors.grey.shade300]),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Icon(Icons.music_note_rounded, color: Colors.grey[500], size: 28),
    );
  }

  String _formatDuration(int durationMillis) {
    final seconds = durationMillis ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }
}
