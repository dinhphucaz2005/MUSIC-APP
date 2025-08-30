package nd.phuc.musicapp.flutter.service.native.core.response

import kotlinx.serialization.Serializable

@Serializable
class AddHouseResponse(
    val houseId: String,
    val message: String
) : NativeResponse()