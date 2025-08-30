package nd.phuc.musicapp.flutter.service.native.impl

import nd.phuc.musicapp.flutter.service.native.abstract.ManageHouseService
import nd.phuc.musicapp.flutter.service.native.core.request.AddHouseRequest
import nd.phuc.musicapp.flutter.service.native.core.response.AddHouseResponse

class ManageHouseServiceImpl : ManageHouseService() {

    override fun addHouse(addHouseRequest: AddHouseRequest): AddHouseResponse {
        return AddHouseResponse(
            houseId = System.currentTimeMillis().toString(),
            message = addHouseRequest.toString()
        )
    }
}