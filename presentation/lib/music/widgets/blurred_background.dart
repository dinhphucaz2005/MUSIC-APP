import "dart:io";
import "dart:ui";
import "package:flutter/material.dart";

class BlurredBackground extends StatelessWidget {
  const BlurredBackground({
    super.key,
    this.thumbnailPath,
  });
  final String? thumbnailPath;

  @override
  Widget build(BuildContext context) {
    if (thumbnailPath == null || thumbnailPath!.isEmpty) {
      return Container(color: Theme.of(context).scaffoldBackgroundColor);
    }

    return RepaintBoundary(
      child: Stack(
        fit: StackFit.expand,
        children: [
          Image.file(
            File(thumbnailPath!),
            fit: BoxFit.cover,
            cacheWidth: 100, // Downsample to 100px width for blur
            errorBuilder: (_, __, ___) => Container(
              color: Theme.of(context).scaffoldBackgroundColor,
            ),
          ),
          BackdropFilter(
            filter: ImageFilter.blur(sigmaX: 30, sigmaY: 30),
            child: Container(
              color: Theme.of(context)
                  .scaffoldBackgroundColor
                  .withValues(alpha: 0.5),
            ),
          ),
        ],
      ),
    );
  }
}
