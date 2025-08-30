package nd.phuc.musicapp.flutter.service.native.abstract

import io.flutter.plugin.common.MethodCall
import nd.phuc.musicapp.flutter.service.native.core.HandlerService
import nd.phuc.musicapp.flutter.service.native.core.request.*
import nd.phuc.musicapp.flutter.service.native.core.response.*
import nd.phuc.musicapp.util.decode

abstract class ManageHouseService : HandlerService() {

    abstract fun addHouse(addHouseRequest: AddHouseRequest): AddHouseResponse

    final override fun provideHandlers(): Map<String, suspend (MethodCall) -> NativeResponse> =
        mapOf(
            "ManageHouseService_addHouse" to {
                addHouse(addHouseRequest = decode(it.arguments.toString()))
            },
        )
}

