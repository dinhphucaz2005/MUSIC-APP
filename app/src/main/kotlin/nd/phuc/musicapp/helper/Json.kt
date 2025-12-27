package nd.phuc.musicapp.helper

import kotlinx.serialization.json.Json


inline fun <reified T> jsonDecode(string: String): T = Json.decodeFromString(string)
