import 'dart:io';
import 'package:flutter/material.dart';

class FullPlayer extends StatelessWidget {
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
  });

  Widget _buildThumbnail() {
    if (thumbnailPath == null || thumbnailPath!.isEmpty) {
      return Container(
        width: double.infinity,
        height: 320,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Colors.deepPurple.shade200, Colors.deepPurple.shade400, Colors.deepPurple.shade600],
          ),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Center(child: Icon(Icons.music_note, color: Colors.white.withValues(alpha: 0.8), size: 100)),
      );
    }

    if (thumbnailPath!.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: Image.file(
          File(thumbnailPath!),
          width: double.infinity,
          height: 320,
          fit: BoxFit.cover,
          errorBuilder:
              (_, __, ___) => Container(
                width: double.infinity,
                height: 320,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [Colors.grey.shade300, Colors.grey.shade400],
                  ),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Icon(Icons.broken_image, color: Colors.grey[600], size: 80),
              ),
        ),
      );
    }

    if (thumbnailPath!.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: Image.network(
          thumbnailPath!,
          width: double.infinity,
          height: 320,
          fit: BoxFit.cover,
          errorBuilder:
              (_, __, ___) => Container(
                width: double.infinity,
                height: 320,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [Colors.grey.shade300, Colors.grey.shade400],
                  ),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Icon(Icons.broken_image, color: Colors.grey[600], size: 80),
              ),
        ),
      );
    }

    return Container(
      width: double.infinity,
      height: 320,
      decoration: BoxDecoration(
        gradient: LinearGradient(begin: Alignment.topLeft, end: Alignment.bottomRight, colors: [Colors.red.shade300, Colors.red.shade400]),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Icon(Icons.error, color: Colors.white, size: 80),
    );
  }

  String _formatDuration(int milliseconds) {
    final seconds = milliseconds ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [Theme.of(context).scaffoldBackgroundColor, Theme.of(context).primaryColor.withValues(alpha: 0.15)],
        ),
      ),
      child: SafeArea(
        child: Column(
          children: [
            // Header with drag indicator and close button
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const SizedBox(width: 40), // Balance
                  Container(width: 50, height: 5, decoration: BoxDecoration(color: Colors.grey[400], borderRadius: BorderRadius.circular(3))),
                  if (onClose != null)
                    IconButton(
                      icon: const Icon(Icons.keyboard_arrow_down, size: 32),
                      onPressed: onClose,
                      padding: EdgeInsets.zero,
                      constraints: const BoxConstraints(),
                    )
                  else
                    const SizedBox(width: 40),
                ],
              ),
            ),
            const SizedBox(height: 10),

            // Album art with shadow and rounded corners
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32),
              child: Hero(
                tag: 'album_art',
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20),
                    boxShadow: [BoxShadow(color: Colors.black.withValues(alpha: 0.3), blurRadius: 30, offset: const Offset(0, 15))],
                  ),
                  child: _buildThumbnail(),
                ),
              ),
            ),
            const SizedBox(height: 40),

            // Song info
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32),
              child: Column(
                children: [
                  Text(
                    title,
                    style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold),
                    textAlign: TextAlign.center,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 8),
                  Text(
                    artist,
                    style: TextStyle(fontSize: 16, color: Colors.grey[600]),
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
                  SliderTheme(
                    data: SliderThemeData(
                      trackHeight: 4,
                      thumbShape: const RoundSliderThumbShape(enabledThumbRadius: 7),
                      overlayShape: const RoundSliderOverlayShape(overlayRadius: 16),
                      activeTrackColor: Theme.of(context).primaryColor,
                      inactiveTrackColor: Colors.grey[300],
                      thumbColor: Theme.of(context).primaryColor,
                      overlayColor: Theme.of(context).primaryColor.withValues(alpha: 0.2),
                    ),
                    child: Slider(
                      value: position.toDouble().clamp(0, duration.toDouble()),
                      max: duration.toDouble() > 0 ? duration.toDouble() : 1,
                      onChanged: onSeek,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(_formatDuration(position), style: TextStyle(fontSize: 13, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                        Text(_formatDuration(duration), style: TextStyle(fontSize: 13, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 32),

            // Controls with better styling
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  // Previous button
                  Material(
                    color: Colors.transparent,
                    child: InkWell(
                      onTap: onPrevious,
                      customBorder: const CircleBorder(),
                      child: Container(
                        padding: const EdgeInsets.all(12),
                        child: Icon(Icons.skip_previous_rounded, size: 40, color: Theme.of(context).primaryColor),
                      ),
                    ),
                  ),
                  // Play/Pause button with elevation
                  Container(
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [Theme.of(context).primaryColor, Theme.of(context).primaryColor.withValues(alpha: 0.8)],
                      ),
                      boxShadow: [
                        BoxShadow(color: Theme.of(context).primaryColor.withValues(alpha: 0.4), blurRadius: 20, offset: const Offset(0, 8)),
                      ],
                    ),
                    child: Material(
                      color: Colors.transparent,
                      child: InkWell(
                        onTap: onPlayPause,
                        customBorder: const CircleBorder(),
                        child: Container(
                          padding: const EdgeInsets.all(20),
                          child: Icon(isPlaying ? Icons.pause_rounded : Icons.play_arrow_rounded, color: Colors.white, size: 40),
                        ),
                      ),
                    ),
                  ),
                  // Next button
                  Material(
                    color: Colors.transparent,
                    child: InkWell(
                      onTap: onNext,
                      customBorder: const CircleBorder(),
                      child: Container(
                        padding: const EdgeInsets.all(12),
                        child: Icon(Icons.skip_next_rounded, size: 40, color: Theme.of(context).primaryColor),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const Spacer(),
          ],
        ),
      ),
    );
  }
}
