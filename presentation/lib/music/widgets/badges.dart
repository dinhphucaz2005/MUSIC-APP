import "package:flutter/material.dart";

/// A reusable count badge widget
class CountBadge extends StatelessWidget {
  const CountBadge({
    required this.count,
    super.key,
    this.suffix,
    this.backgroundColor,
    this.textColor,
  });

  final int count;
  final String? suffix;
  final Color? backgroundColor;
  final Color? textColor;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final bgColor =
        backgroundColor ?? theme.colorScheme.primary.withValues(alpha: 0.1);
    final fgColor = textColor ?? theme.colorScheme.primary;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(
        suffix != null ? "$count $suffix" : "$count",
        style: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          color: fgColor,
        ),
      ),
    );
  }
}

/// A duration badge that formats milliseconds to mm:ss
class DurationBadge extends StatelessWidget {
  const DurationBadge({
    required this.durationMillis,
    super.key,
    this.isHighlighted = false,
  });

  final int durationMillis;
  final bool isHighlighted;

  String _formatDuration(int millis) {
    final seconds = millis ~/ 1000;
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes:${remainingSeconds.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: isHighlighted
            ? theme.colorScheme.primary.withValues(alpha: 0.1)
            : theme.colorScheme.primary,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        _formatDuration(durationMillis),
        style: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w500,
          color: isHighlighted
              ? theme.colorScheme.primary
              : theme.colorScheme.primaryContainer,
        ),
      ),
    );
  }
}
