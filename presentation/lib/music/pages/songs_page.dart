import 'package:flutter/material.dart';

import 'package:music/media_controller_manager.dart';
import 'package:music/local_song_repository.dart';
import 'package:music/song.dart';
import 'package:presentation/music/widgets/song_item.dart';

class SongsPage extends StatefulWidget {
  final LocalSongRepository repository;
  final MediaControllerManager mediaController;

  const SongsPage({super.key, required this.repository, required this.mediaController});

  @override
  State<SongsPage> createState() => _SongsPageState();
}

class _SongsPageState extends State<SongsPage> {
  @override
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Theme.of(context).primaryColor.withValues(alpha: 0.1),
              Theme.of(context).scaffoldBackgroundColor,
            ],
            stops: const [0.0, 0.3],
          ),
        ),
        child: StreamBuilder<LocalSongRepositoryState>(
          stream: widget.repository.subject,
          builder: (context, snapshot) {
            final state = snapshot.data ?? LocalSongRepositoryState();
            final songs = state.songs;

            if (state.isLoading && songs.isEmpty) {
              return const Center(child: CircularProgressIndicator());
            }

            if (songs.isEmpty) {
              return CustomScrollView(
                slivers: [
                  _buildSliverAppBar(0),
                  SliverFillRemaining(
                    child: Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                            padding: const EdgeInsets.all(24),
                            decoration: BoxDecoration(
                              color: Theme.of(context).primaryColor.withValues(alpha: 0.1),
                              shape: BoxShape.circle,
                            ),
                            child: Icon(Icons.music_note_rounded, size: 64, color: Theme.of(context).primaryColor),
                          ),
                          const SizedBox(height: 24),
                          Text(
                            'No songs found',
                            style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Theme.of(context).textTheme.bodyLarge?.color),
                          ),
                          const SizedBox(height: 8),
                          Text(
                            'Load your local music library to get started',
                            style: TextStyle(fontSize: 16, color: Colors.grey[600]),
                          ),
                          const SizedBox(height: 32),
                          ElevatedButton.icon(
                            onPressed: () => widget.repository.loadLocalSongs(),
                            icon: const Icon(Icons.refresh_rounded),
                            label: const Text('Load Songs'),
                            style: ElevatedButton.styleFrom(
                              padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
                              elevation: 0,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              );
            }

            return StreamBuilder<PlayerState>(
              stream: widget.mediaController.subject,
              builder: (context, playerSnapshot) {
                final playerState = playerSnapshot.data;

                return CustomScrollView(
                  physics: const BouncingScrollPhysics(),
                  slivers: [
                    _buildSliverAppBar(songs.length),
                    if (state.isLoading)
                      const SliverToBoxAdapter(
                        child: LinearProgressIndicator(minHeight: 2),
                      ),
                    SliverPadding(
                      padding: const EdgeInsets.only(bottom: 100), // Space for mini player
                      sliver: SliverList(
                        delegate: SliverChildBuilderDelegate(
                          (context, index) {
                            final song = songs[index];
                            final playerSong = playerState?.currentSong;
                            final isPlaying = playerState != null && playerSong is LocalSong && playerSong.title == song.title && (playerState.isPlaying ?? false);
                            return Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                              child: SongItem(
                                song: song,
                                isPlaying: isPlaying,
                                onTap: () => widget.mediaController.playSong(song),
                              ),
                            );
                          },
                          childCount: songs.length,
                        ),
                      ),
                    ),
                  ],
                );
              },
            );
          },
        ),
      ),
    );
  }

  Widget _buildSliverAppBar(int songCount) {
    return SliverAppBar(
      expandedHeight: 80.0,
      floating: true,
      pinned: true,
      stretch: true,
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      surfaceTintColor: Colors.transparent,
      flexibleSpace: FlexibleSpaceBar(
        titlePadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
        title: Row(
          children: [
            Text(
              'My Music',
              style: TextStyle(color: Theme.of(context).textTheme.bodyLarge?.color, fontWeight: FontWeight.bold),
            ),
            if (songCount > 0) ...[
              const SizedBox(width: 8),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                decoration: BoxDecoration(
                  color: Theme.of(context).primaryColor.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  '$songCount',
                  style: TextStyle(fontSize: 12, color: Theme.of(context).primaryColor, fontWeight: FontWeight.bold),
                ),
              ),
            ],
          ],
        ),
        background: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Theme.of(context).primaryColor.withValues(alpha: 0.05),
                Theme.of(context).scaffoldBackgroundColor.withValues(alpha: 0.0),
              ],
            ),
          ),
        ),
      ),
      actions: [
        const SizedBox(width: 8),
      ],
    );
  }
}
