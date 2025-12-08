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
    this.speed = 1.2, // Tốc độ mặc định 1.2
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
    _amps = List.generate(widget.barCount, (i) => 0.3 + (i % 3) * 0.2 + _rnd.nextDouble() * 0.4);
    _freqOffsets = List.generate(widget.barCount, (_) => _rnd.nextDouble() * 0.2);
    _values = List.filled(widget.barCount, 0);
    _velocity = List.filled(widget.barCount, 0);

    // Tăng duration để chậm hơn và mượt hơn
    final duration = Duration(milliseconds: max(800, (2500 / widget.speed).round()));

    _controller = AnimationController(vsync: this, duration: duration)..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
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

            // Tạo frequency khác nhau cho từng thanh để chúng không đồng bộ
            final baseFreq = 0.3 + (i * 0.12) + _freqOffsets[i];
            final slowFreq = 0.15 + (i % 3) * 0.06;
            final midFreq = 0.45 + (i % 4) * 0.08;

            // Jitter nhẹ cho tự nhiên
            final jitter = 0.02 * sin(t * 2 * pi * 2.5 + i * 2.1);

            // Kết hợp nhiều sóng với amplitude khác nhau
            double v = sin(t * 2 * pi * baseFreq + phase);
            v += 0.4 * sin(t * 2 * pi * slowFreq + phase * 1.3);
            v += 0.3 * sin(t * 2 * pi * midFreq + phase * 0.7);

            // Normalize về 0-1 (tổng amplitude tối đa = 1 + 0.4 + 0.3 = 1.7)
            v = (v + 1.7) / 3.4;

            // Thêm variation giữa các thanh để chiều cao chênh lệch nhiều hơn
            final heightVariation = 0.1 + (i % 4) * 0.25;
            v = (v * _amps[i] + heightVariation + jitter).clamp(0, 1);

            // Smooth curves để mượt hơn
            v = Curves.easeInOutSine.transform(v);

            final target = v;

            // Spring physics mượt mà hơn
            final spring = 0.08; // Giảm spring force để chậm hơn
            final damping = 0.82; // Tăng damping để mượt hơn

            final force = (target - _values[i]) * spring;
            _velocity[i] += force;
            _velocity[i] *= damping;

            _values[i] += _velocity[i];
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
    // Glow effect nhẹ hơn
    final glowPaint = Paint()
      ..color = color.withValues(alpha: 0.3)
      ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 4);

    // Gradient đẹp hơn
    final gradientPaint = Paint()
      ..shader = LinearGradient(
        colors: [
          color.withValues(alpha: 0.9),
          color.withValues(alpha: 0.7),
          color.withValues(alpha: 0.5),
        ],
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
      ).createShader(Rect.fromLTWH(0, 0, size.width, size.height));

    final barWidth = (size.width - spacing * (barCount - 1)) / barCount;
    final maxHeight = size.height;
    final minHeight = maxHeight * 0.12; // Tăng min height để dễ thấy hơn

    for (int i = 0; i < barCount; i++) {
      // Tính height với chênh lệch lớn hơn
      final heightRatio = values[i];
      final height = minHeight + (maxHeight - minHeight) * heightRatio;
      final left = i * (barWidth + spacing);

      final rect = Rect.fromLTWH(left, maxHeight - height, barWidth, height);
      final roundedRect = RRect.fromRectAndRadius(rect, Radius.circular(cornerRadius));

      // Vẽ glow
      canvas.drawRRect(roundedRect.inflate(1.5), glowPaint);

      // Vẽ thanh chính
      canvas.drawRRect(roundedRect, gradientPaint);
    }
  }

  @override
  bool shouldRepaint(_) => true;
}
