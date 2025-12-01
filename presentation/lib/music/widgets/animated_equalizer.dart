import 'dart:math';
import 'package:flutter/material.dart';

class AnimatedEqualizer extends StatefulWidget {
  final Color color;
  final double size;
  final int barCount;
  final double speed;
  final double spacing;
  final double cornerRadius;

  const AnimatedEqualizer({
    super.key,
    this.color = Colors.white,
    this.size = 32,
    this.barCount = 5,
    this.speed = 0.5,
    this.spacing = 2,
    this.cornerRadius = 2.0,
  });

  @override
  State<AnimatedEqualizer> createState() => _AnimatedEqualizerState();
}

class _AnimatedEqualizerState extends State<AnimatedEqualizer> with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  late final Random _rnd;

  late final List<double> _phases;
  late final List<double> _amps;
  late final List<double> _freqOffsets;
  late final List<double> _values;
  late final List<double> _velocity;

  @override
  void initState() {
    super.initState();
    _rnd = Random();

    _phases = List.generate(widget.barCount, (_) => _rnd.nextDouble() * pi * 2);
    _amps = List.generate(widget.barCount, (_) => 0.5 + _rnd.nextDouble() * 1.0);
    _freqOffsets = List.generate(widget.barCount, (_) => _rnd.nextDouble() * 0.1);
    _values = List.filled(widget.barCount, 0);
    _velocity = List.filled(widget.barCount, 0);

    final duration = Duration(milliseconds: max(300, (1500 / widget.speed).round()));

    _controller = AnimationController(vsync: this, duration: duration)..repeat();
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: widget.size,
      height: widget.size,
      child: AnimatedBuilder(
        animation: _controller,
        builder: (_, __) {
          final t = _controller.value;

          for (int i = 0; i < widget.barCount; i++) {
            final phase = _phases[i];

            final freq = 0.7 + (i % 5) * 0.2 + _freqOffsets[i];
            final low = 0.2 + (i % 3) * 0.12;
            final mid = 0.4 + (i % 4) * 0.15;
            
            // Subtle jitter for organic feeling
            final jitter = 0.03 * sin(t * 2 * pi * 5 + i * 1.8);

            // Main frequency component
            double v = sin(t * 2 * pi * freq + phase);
            
            // Add harmonics với amplitudes thấp hơn để tránh overflow
            v += 0.25 * sin(t * 2 * pi * low + phase * 0.8);
            v += 0.15 * sin(t * 2 * pi * mid + phase * 1.3);
            
            // Normalize properly - chia cho tổng amplitude tối đa (1 + 0.25 + 0.15 = 1.4)
            v = v / 1.4;
            
            // Convert to 0-1 range
            v = (v + 1) / 2;

            // Apply power curve
            v = pow(v, 1.2).toDouble() * _amps[i];
            v = (v + jitter).clamp(0, 1);

            // Smooth easing
            v = Curves.easeOutCubic.transform(v);

            double target = v;
            
            // Spring physics mềm hơn để mượt mà tự nhiên
            double force = (target - _values[i]) * 0.18;

            _velocity[i] += force;
            _velocity[i] *= 0.86; // Giảm damping để responsive hơn

            _values[i] += _velocity[i];
            
            // QUAN TRỌNG: Clamp giá trị cuối để không vượt khung
            _values[i] = _values[i].clamp(0.0, 1.0);
          }

          return CustomPaint(
            painter: _EqualizerPainter(
              color: widget.color,
              values: List.of(_values),
              barCount: widget.barCount,
              spacing: widget.spacing,
              cornerRadius: widget.cornerRadius,
            ),
          );
        },
      ),
    );
  }
}

class _EqualizerPainter extends CustomPainter {
  final Color color;
  final List<double> values;
  final int barCount;
  final double spacing;
  final double cornerRadius;

  _EqualizerPainter({required this.color, required this.values, required this.barCount, required this.spacing, required this.cornerRadius});

  @override
  void paint(Canvas canvas, Size size) {
    final glowPaint =
        Paint()
          ..color = color.withOpacity(0.25)
          ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 6);

    final grad =
        Paint()
          ..shader = LinearGradient(
            colors: [color.withOpacity(0.95), color.withOpacity(0.6)],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ).createShader(Rect.fromLTWH(0, 0, size.width, size.height));

    final barWidth = (size.width - spacing * (barCount - 1)) / barCount;
    final maxH = size.height;
    final minH = maxH * 0.06;

    for (int i = 0; i < barCount; i++) {
      final h = minH + (maxH - minH) * values[i];
      final left = i * (barWidth + spacing);

      final rect = Rect.fromLTWH(left, maxH - h, barWidth, h);
      final r = RRect.fromRectAndRadius(rect, Radius.circular(cornerRadius));

      canvas.drawRRect(r.inflate(2), glowPaint);
      canvas.drawRRect(r, grad);
    }
  }

  @override
  bool shouldRepaint(_) => true;
}
