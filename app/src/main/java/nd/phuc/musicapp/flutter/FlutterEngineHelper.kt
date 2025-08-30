package nd.phuc.musicapp.flutter

import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import nd.phuc.musicapp.flutter.service.native.impl.ManageHouseServiceImpl

object FlutterEngineHelper {

    private const val FLUTTER_ENGINE_ID = "aio_audio_engine"
    private const val CHANNEL_NAME = "nd.phuc.musicapp/audio"

    var flutterEngine: FlutterEngine? = null
    private var methodChannel: MethodChannel? = null

    fun initializeFlutter(context: Context) {
        if (flutterEngine == null) {
            flutterEngine = FlutterEngine(context).apply {
                dartExecutor.executeDartEntrypoint(
                    DartExecutor.DartEntrypoint.createDefault()
                )
            }
            GeneratedPluginRegistrant.registerWith(flutterEngine!!)
            FlutterEngineCache.getInstance().put(FLUTTER_ENGINE_ID, flutterEngine!!)

            methodChannel = MethodChannel(
                flutterEngine!!.dartExecutor.binaryMessenger,
                CHANNEL_NAME
            )
            MethodChannelHandler(
                methodChannel!!,
                manageHouseService = ManageHouseServiceImpl(),
                scope = CoroutineScope(Dispatchers.IO)
            )

        }
    }


    fun launchFlutterAudioPlayer(context: Context) {
        val intent = FlutterActivity
            .withCachedEngine(FLUTTER_ENGINE_ID)
            .build(context)
        context.startActivity(intent)
    }

    fun createFlutterFragment(): FlutterFragment {
        return FlutterFragment
            .withCachedEngine(FLUTTER_ENGINE_ID)
            .renderMode(RenderMode.texture)
            .build()
    }

    /**
     * Clean up resources
     */
    fun dispose() {
        methodChannel?.setMethodCallHandler(null)
        flutterEngine?.destroy()
        FlutterEngineCache.getInstance().remove(FLUTTER_ENGINE_ID)
        flutterEngine = null
        methodChannel = null
    }
}
