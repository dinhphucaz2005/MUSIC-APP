package nd.phuc.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
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
import nd.phuc.music.model.ThumbnailSource

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
    private lateinit var playerEventChannel: EventChannel
    private var applicationContext: Context? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val songEventChannel = Channel<Map<String, Any?>>(Channel.BUFFERED)
    private val playerStateChannel = Channel<Map<String, Any?>>(Channel.BUFFERED)

    private val playerStateReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MusicService.ACTION_PLAYER_STATE_CHANGED) {
                val stateMap = mapOf(
                    "isPlaying" to intent.getBooleanExtra(
                        MusicService.ACTION_PLAYER_STATE_CHANGED_IS_PLAYING,
                        false
                    ),
                    "title" to intent.getStringExtra(
                        MusicService.ACTION_PLAYER_STATE_CHANGED_TITLE
                    ),
                    "artist" to intent.getStringExtra(
                        MusicService.ACTION_PLAYER_STATE_CHANGED_ARTIST
                    ),
                    "duration" to intent.getLongExtra(
                        MusicService.ACTION_PLAYER_STATE_CHANGED_DURATION,
                        0L
                    ),
                    "position" to intent.getLongExtra(
                        MusicService.ACTION_PLAYER_STATE_CHANGED_POSITION,
                        0L
                    )
                )
                playerStateChannel.trySend(stateMap)
            }
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "music")
        channel.setMethodCallHandler(this)

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "music_events")
        eventChannel.setStreamHandler(this)

        playerEventChannel =
            EventChannel(flutterPluginBinding.binaryMessenger, "music_player_events")
        playerEventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                scope.launch {
                    playerStateChannel.receiveAsFlow().collect { state ->
                        events?.success(state)
                    }
                }
            }

            override fun onCancel(arguments: Any?) {}
        })

        applicationContext = flutterPluginBinding.applicationContext
        LocalSongExtractor.initialize(applicationContext!!)

        val filter = android.content.IntentFilter(MusicService.ACTION_PLAYER_STATE_CHANGED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext?.registerReceiver(
                playerStateReceiver, filter, Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            ContextCompat.registerReceiver(
                applicationContext!!,
                playerStateReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
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
                        when (result) {
                            is ExtractLocalSongResult.Success -> {
                                val song = result.song
                                accumulatedSongs.add(song)

                                val songMap = mapOf(
                                    "title" to song.title,
                                    "artist" to song.artist,
                                    "filePath" to song.filePath,
                                    "duration" to song.durationMillis,
                                    "thumbnailPath" to when (val thumb = song.thumbnailSource) {
                                        is ThumbnailSource.FilePath -> thumb.path
                                        is ThumbnailSource.FromUrl -> thumb.url
                                        else -> null
                                    }
                                )
                                songEventChannel.trySend(mapOf("type" to "add", "song" to songMap))
                            }

                            is ExtractLocalSongResult.NotFound -> {
                                songEventChannel.trySend(
                                    mapOf(
                                        "type" to "remove", "id" to result.path
                                    )
                                )
                            }

                            is ExtractLocalSongResult.Finished -> {
                                songEventChannel.trySend(mapOf("type" to "finish"))
                            }

                            is ExtractLocalSongResult.Error -> {}
                            ExtractLocalSongResult.Idle -> {}
                            ExtractLocalSongResult.InProgress -> {}
                        }
                    },
                )
                result.success(null)
            }

            "startService" -> {
                val intent = Intent(ctx, serviceClass)
                ctx.startForegroundService(intent)
                result.success(null)
            }

            "stopService" -> {
                val intent = Intent(ctx, serviceClass)
                ctx.stopService(intent)
                result.success(null)
            }

            "playOrPause" -> {
                MediaControllerManager.togglePlayPause()
                result.success(null)
            }

            "next" -> {
                MediaControllerManager.playNextSong()
                result.success(null)
            }

            "previous" -> {
                MediaControllerManager.playPreviousSong()
                result.success(null)
            }

            "toggleShuffle" -> {
                MediaControllerManager.toggleShuffle()
                result.success(null)
            }

            "toggleRepeat" -> {
                MediaControllerManager.toggleRepeat()
                result.success(null)
            }

            "playAtIndex" -> {
                throw NotImplementedError("playAtIndex is not implemented yet")
            }

            "playSong" -> {
                val filePath = call.argument<String>("path") ?: ""
                val song = LocalSongExtractor.getCachedSong(filePath)
                if (song != null) {
                    MediaControllerManager.play(song)
                    result.success(null)
                } else {
                    result.error("SONG_NOT_FOUND", "Song not found for path: $filePath", null)
                }
            }

            else -> result.notImplemented()
        }
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext?.unregisterReceiver(playerStateReceiver)
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        playerEventChannel.setStreamHandler(null)
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
