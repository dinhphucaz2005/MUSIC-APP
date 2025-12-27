package nd.phuc.musicapp.service.music

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.net.toUri
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.extractor.mkv.MatroskaExtractor
import androidx.media3.extractor.mp3.Mp3Extractor
import androidx.media3.extractor.mp4.FragmentedMp4Extractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.io.File

class CustomMediaSourceFactory(
    private val context: Context,
) {


    @SuppressLint("UnsafeOptInUsageError")
    private fun createCacheDataSource(context: Context): CacheDataSource.Factory {

        @Suppress("DEPRECATION") val downloadCache = SimpleCache(
            File(context.cacheDir, "media"),
            NoOpCacheEvictor()
        )

        val upstreamFactory = DefaultDataSource.Factory(
            context,
            OkHttpDataSource.Factory(OkHttpClient.Builder().build())
        )
        return CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setCacheWriteDataSinkFactory(null)
            .setFlags(FLAG_IGNORE_CACHE_ON_ERROR)
    }


    @SuppressLint("UnsafeOptInUsageError")
    private fun createDataSourceFactory(context: Context): DataSource.Factory =
        ResolvingDataSource.Factory(createCacheDataSource(context)) { dataSpec ->
            /**
             * Set the custom cache key using the YouTube media ID.
             * The YouTube media ID (youtubeMediaId) is extracted from the dataSpec key, which is used as a custom cache key for retrieving the media.
             * - The customCacheKey will be used by the MusicService to identify and fetch the corresponding media from the YouTube API.
             * The youtubeMediaId is essentially the identifier that allows you to retrieve the exact media from YouTube.
             * See Example in [nd.phuc.musicapp.extension.toMediaItemFromYT]
             */
            val youtubeMediaId = dataSpec.key

            /**
             * This feature is disabled because it is in testing phase.
             * Use room to cache the media URL. (In the cacheRepository, insert the media URL into the database using the youtubeMediaId as the key.)
             */
//            val url = if (youtubeMediaId != null) {
//                runBlocking(Dispatchers.IO) {
//                    return@runBlocking cacheRepository.getUrlFromSongId(youtubeMediaId)
//                        ?: CustomYoutube.player(youtubeMediaId)
//                            .getOrNull()
//                            ?.streamingData
//                            ?.adaptiveFormats
//                            ?.filter { it.isAudio }
//                            ?.maxByOrNull {
//                                it.bitrate + (if (it.mimeType.startsWith("audio/webm")) 10240 else 0)
//                            }?.url?.also { cacheRepository.insertSong(youtubeMediaId, it) }
//                }
//            } else null

            val url = if (youtubeMediaId != null) {
                runBlocking(Dispatchers.IO) {
                    return@runBlocking "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
//                    return@runBlocking CustomYoutube.player(youtubeMediaId)
//                        .getOrNull()
//                        ?.streamingData
//                        ?.adaptiveFormats
//                        ?.filter { it.isAudio }
//                        ?.maxByOrNull {
//                            it.bitrate + (if (it.mimeType.startsWith("audio/webm")) 10240 else 0)
//                        }?.url
                }
            } else null

            dataSpec.withUri(url?.toUri() ?: dataSpec.uri)
        }


    @SuppressLint("UnsafeOptInUsageError")
    fun getInstance(): MediaSource.Factory =
        DefaultMediaSourceFactory(createDataSourceFactory(context)) {
            @Suppress("DEPRECATION") arrayOf(
                MatroskaExtractor(),
                FragmentedMp4Extractor(),
                Mp3Extractor()
            )
        }
}