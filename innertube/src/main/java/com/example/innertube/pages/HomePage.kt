package com.example.innertube.pages

import com.example.innertube.models.AlbumItem
import com.example.innertube.models.Artist
import com.example.innertube.models.ArtistItem
import com.example.innertube.models.BrowseEndpoint
import com.example.innertube.models.MusicCarouselShelfRenderer
import com.example.innertube.models.MusicTwoRowItemRenderer
import com.example.innertube.models.PlaylistItem
import com.example.innertube.models.SongItem
import com.example.innertube.models.YTItem
import com.example.innertube.models.oddElements

data class HomePage(
    val sections: List<Section>,
) {
    sealed class Section {
        abstract val title: String
        abstract val label: String?
        abstract val thumbnail: String?
        abstract val endpoint: BrowseEndpoint?
        abstract val items: List<YTItem>

        companion object {
            fun fromMusicCarouselShelfRenderer(renderer: MusicCarouselShelfRenderer): Section? {
                val title =
                    renderer.header?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.firstOrNull()?.text
                        ?: return null
                val label =
                    renderer.header.musicCarouselShelfBasicHeaderRenderer.strapline?.runs?.firstOrNull()?.text
                val thumbnail =
                    renderer.header.musicCarouselShelfBasicHeaderRenderer.thumbnail?.musicThumbnailRenderer?.getThumbnailUrl()
                val endpoint =
                    renderer.header.musicCarouselShelfBasicHeaderRenderer.moreContentButton?.buttonRenderer?.navigationEndpoint?.browseEndpoint
                val items = renderer.contents.mapNotNull {
                    it.musicTwoRowItemRenderer
                }.mapNotNull {
                    fromMusicTwoRowItemRenderer(it)
                }
                if (items.isEmpty()) return null

                return when (items.first()) {
                    is SongItem -> Song(
                        title = title,
                        label = label,
                        thumbnail = thumbnail,
                        endpoint = endpoint,
                        items = items.filterIsInstance<SongItem>()
                    )

                    is AlbumItem -> {
                        null
                    }

                    is ArtistItem -> {
                        null
                    }

                    is PlaylistItem -> Playlist(
                        title = title,
                        label = label,
                        thumbnail = thumbnail,
                        endpoint = endpoint,
                        items = items.filterIsInstance<PlaylistItem>()
                    )
                }

            }

            private fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): YTItem? {
                return when {
                    renderer.isSong -> {
                        SongItem(
                            id = renderer.navigationEndpoint.watchEndpoint?.videoId ?: return null,
                            title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                            artists = listOfNotNull(renderer.subtitle?.runs?.firstOrNull()?.let {
                                Artist(
                                    name = it.text,
                                    id = it.navigationEndpoint?.browseEndpoint?.browseId
                                )
                            }),
                            album = null,
                            duration = null,
                            thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                            explicit = renderer.subtitleBadges?.find {
                                it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                            } != null
                        )
                    }

                    renderer.isAlbum -> {
                        AlbumItem(
                            browseId = renderer.navigationEndpoint.browseEndpoint?.browseId
                                ?: return null,
                            playlistId = renderer.thumbnailOverlay?.musicItemThumbnailOverlayRenderer?.content
                                ?.musicPlayButtonRenderer?.playNavigationEndpoint
                                ?.watchPlaylistEndpoint?.playlistId ?: return null,
                            title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                            artists = renderer.subtitle?.runs?.oddElements()?.drop(1)?.map {
                                Artist(
                                    name = it.text,
                                    id = it.navigationEndpoint?.browseEndpoint?.browseId
                                )
                            },
                            year = renderer.subtitle?.runs?.lastOrNull()?.text?.toIntOrNull(),
                            thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                            explicit = renderer.subtitleBadges?.find {
                                it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                            } != null
                        )
                    }

                    renderer.isPlaylist -> {
                        // Playlist from YouTube Music
                        PlaylistItem(
                            id = renderer.navigationEndpoint.browseEndpoint?.browseId?.removePrefix(
                                "VL"
                            ) ?: return null,
                            title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                            author = Artist(
                                name = renderer.subtitle?.runs?.lastOrNull()?.text ?: return null,
                                id = null
                            ),
                            songCountText = null,
                            thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                            playEndpoint = renderer.thumbnailOverlay
                                ?.musicItemThumbnailOverlayRenderer?.content
                                ?.musicPlayButtonRenderer?.playNavigationEndpoint
                                ?.watchPlaylistEndpoint ?: return null,
                            shuffleEndpoint = renderer.menu?.menuRenderer?.items?.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MUSIC_SHUFFLE"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                            radioEndpoint = renderer.menu.menuRenderer.items.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MIX"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null
                        )
                    }

                    renderer.isArtist -> {
                        ArtistItem(
                            id = renderer.navigationEndpoint.browseEndpoint?.browseId
                                ?: return null,
                            title = renderer.title.runs?.lastOrNull()?.text ?: return null,
                            thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                            shuffleEndpoint = renderer.menu?.menuRenderer?.items?.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MUSIC_SHUFFLE"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                            radioEndpoint = renderer.menu.menuRenderer.items.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MIX"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                        )
                    }

                    else -> null
                }
            }
        }

        data class Song(
            override val title: String,
            override val label: String?,
            override val thumbnail: String?,
            override val endpoint: BrowseEndpoint?,
            override val items: List<SongItem>,
        ) : Section()

        data class Album(
            override val title: String,
            override val label: String?,
            override val thumbnail: String?,
            override val endpoint: BrowseEndpoint?,
            override val items: List<AlbumItem>,
        ) : Section()

        data class Artist(
            override val title: String,
            override val label: String?,
            override val thumbnail: String?,
            override val endpoint: BrowseEndpoint?,
            override val items: List<ArtistItem>,
        ) : Section()

        data class Playlist(
            override val title: String,
            override val label: String?,
            override val thumbnail: String?,
            override val endpoint: BrowseEndpoint?,
            override val items: List<PlaylistItem>,
        ) : Section()
    }


}



