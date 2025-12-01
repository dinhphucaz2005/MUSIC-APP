import 'dart:io';
import 'package:flutter/material.dart';

class AlbumArtTransition extends StatelessWidget {
  final String? thumbnailPath;
  final double progress;
  final double miniSize;
  final double fullSize;

  const AlbumArtTransition({
    super.key,
    required this.thumbnailPath,
    required this.progress,
    this.miniSize = 48,
    this.fullSize = 280,
  });

  Widget _buildPlaceholder(double size, {bool isError = false}) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        color: Colors.grey[300],
        borderRadius: BorderRadius.circular(size * 0.1),
      ),
      child: Icon(
        isError ? Icons.broken_image : Icons.music_note,
        color: Colors.grey[600],
        size: size * 0.4,
      ),
    );
  }

  Widget _buildImage(double size) {
    if (thumbnailPath == null || thumbnailPath!.isEmpty) {
      return _buildPlaceholder(size);
    }

    if (thumbnailPath!.startsWith('/')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(size * 0.1),
        child: Image.file(
          File(thumbnailPath!),
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(size, isError: true),
        ),
      );
    }

    if (thumbnailPath!.startsWith('http')) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(size * 0.1),
        child: Image.network(
          thumbnailPath!,
          width: size,
          height: size,
          fit: BoxFit.cover,
          errorBuilder: (_, __, ___) => _buildPlaceholder(size, isError: true),
        ),
      );
    }

    return _buildPlaceholder(size, isError: true);
  }

  @override
  Widget build(BuildContext context) {
    final size = miniSize + (fullSize - miniSize) * progress;
    final borderRadius = BorderRadius.circular(8 + 12 * progress);

    return Container(
      decoration: BoxDecoration(
        borderRadius: borderRadius,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.2 + 0.1 * progress),
            blurRadius: 8 + 12 * progress,
            offset: Offset(0, 2 + 4 * progress),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: borderRadius,
        child: _buildImage(size),
      ),
    );
  }
}

