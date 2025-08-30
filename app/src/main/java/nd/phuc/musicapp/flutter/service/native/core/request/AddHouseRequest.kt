package nd.phuc.musicapp.flutter.service.native.core.request

import kotlinx.serialization.Serializable

@Serializable
class AddHouseRequest(
    val testBoolean: Boolean,
    val testLong: Int,
    val testDouble: Double,
    val testString: String,
    val testByteArray: ByteArray,
    val testIntArray: IntArray,
    val testLongArray: LongArray,
    val testFloatArray: FloatArray,
    val testDoubleArray: DoubleArray,
    val testList: List<String>,
) : FlutterRequest()