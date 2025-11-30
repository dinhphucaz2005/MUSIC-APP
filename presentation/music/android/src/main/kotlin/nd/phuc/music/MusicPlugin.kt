package nd.phuc.music

import android.content.Context
import android.content.Intent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import nd.phuc.music.model.LocalSong

class MusicPlugin(
) : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {

    companion object {
        private lateinit var serviceClass: Class<out MusicService>

        @JvmStatic
        fun registerWith(
            service: Class<out MusicService>,
        ) {
            serviceClass = service
        }
    }

    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var applicationContext: Context? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val songEventChannel = Channel<Map<String, Any?>>(Channel.BUFFERED)

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "music")
        channel.setMethodCallHandler(this)

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "music_events")
        eventChannel.setStreamHandler(this)

        applicationContext = flutterPluginBinding.applicationContext
        LocalSongExtractor.initialize(applicationContext!!)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        val ctx = applicationContext
        if (ctx == null) {
            result.error("NO_CONTEXT", "Plugin not attached to context", null)
            return
        }

        when (call.method) {
            "loadLocal" -> {
                val accumulatedSongs = mutableListOf<LocalSong>()

                // Emit start event
                songEventChannel.trySend(mapOf("type" to "start"))

                LocalSongExtractor.extracts(
                    context = ctx,
                    filePaths = emptyList(),
                    onSongExtracted = { result ->
                        if (result is ExtractLocalSongResult.Success) {
                            val song = result.song
                            accumulatedSongs.add(song)

                            val songMap = mapOf(
                                "title" to song.title,
                                "artist" to song.artist,
                                "filePath" to song.filePath,
                                "duration" to song.durationMillis,
                                "thumbnailPath" to when (val thumb = song.thumbnailSource) {
                                    is nd.phuc.music.model.ThumbnailSource.FilePath -> thumb.path
                                    is nd.phuc.music.model.ThumbnailSource.FromUrl -> thumb.url
                                    else -> null
                                }
                            )
                            songEventChannel.trySend(mapOf("type" to "add", "song" to songMap))
                        } else if (result is ExtractLocalSongResult.NotFound) {
                            songEventChannel.trySend(mapOf("type" to "remove", "id" to result.path))
                        } else if (result is ExtractLocalSongResult.Finished) {
                            songEventChannel.trySend(mapOf("type" to "finish"))
                        }
                    },
                )
                result.success(null)
            }

            "startService" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                ctx.startForegroundServiceCompat(intent)
                result.success(null)
            }

            "stopService" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                ctx.stopService(intent)
                result.success(null)
            }

            "playOrPause" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_PLAY_OR_PAUSE
                ctx.startService(intent)
                result.success(null)
            }

            "next" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_NEXT
                ctx.startService(intent)
                result.success(null)
            }

            "previous" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_PREVIOUS
                ctx.startService(intent)
                result.success(null)
            }

            "toggleShuffle" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_SHUFFLE
                ctx.startService(intent)
                result.success(null)
            }

            "toggleRepeat" -> {
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_REPEAT
                ctx.startService(intent)
                result.success(null)
            }

            "playAtIndex" -> {
                val index = call.argument<Int>("index") ?: 0
                val intent = Intent(ctx, serviceClass::class.java)
                intent.action = MusicService.ACTION_PLAY_OR_PAUSE
                intent.putExtra("index", index)
                ctx.startService(intent)
                result.success(null)
            }

            else -> result.notImplemented()
        }
    }

    private fun Context.startForegroundServiceCompat(intent: Intent) {
        this.startForegroundService(intent)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        scope.cancel()
        applicationContext = null
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        val ctx = applicationContext
        if (ctx == null || events == null) return

        scope.launch {
            songEventChannel.receiveAsFlow().collect { songMap ->
                events.success(songMap)
            }
        }
    }

    override fun onCancel(arguments: Any?) {
    }
}
