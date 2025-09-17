/*
package nd.phuc.musicapp.flutter


import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import nd.phuc.core.extension.withMainContext
import nd.phuc.musicapp.flutter.service.native.abstract.ManageHouseService
import nd.phuc.musicapp.flutter.service.native.core.response.*

class MethodChannelHandler(
    channel: MethodChannel,
    manageHouseService: ManageHouseService,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    private val handlers = mutableMapOf<String, suspend (MethodCall) -> NativeResponse>()
    private val handlerService = listOf(
        manageHouseService,
    )

    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(NativeResponse::class) {
                subclass(AddHouseResponse::class, AddHouseResponse.serializer())
            }
        }
    }

    init {
        handlerService.forEach { service ->
            service.provideHandlers().forEach { (method, handler) ->
                if (handlers.contains(method)) throw Exception("Method '$method' is already registered in MethodChannelHandler")
                handlers[method] = handler
            }
        }
        channel.setMethodCallHandler { call, result ->
            val handler = handlers[call.method]
            if (handler == null) {
                result.notImplemented()
                return@setMethodCallHandler
            }

            scope.launch {
                try {
                    val res: NativeResponse = handler(call)
                    val jsonString = json.encodeToString(
                        serializer = PolymorphicSerializer(NativeResponse::class),
                        value = res
                    )
                    withMainContext { result.success(jsonString) }
                } catch (e: Exception) {
                    withMainContext {
                        result.error(
                            System.currentTimeMillis().toString(),
                            e.message,
                            e.stackTraceToString()
                        )
                    }
                }
            }
        }
    }
}
*/
