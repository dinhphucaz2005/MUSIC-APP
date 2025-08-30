package nd.phuc.musicapp.util

import kotlinx.serialization.json.Json

val json = Json {}

inline fun <reified T> decode(string: String): T = json.decodeFromString(string)
