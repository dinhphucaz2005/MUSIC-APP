package nd.phuc.musicapp.helper

import kotlin.collections.get

inline fun <reified T> Map<*, *>.get(key: String): T {
    return this[key] as T
}