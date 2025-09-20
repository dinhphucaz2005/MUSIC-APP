package nd.phuc.core.helper

import kotlin.collections.get

inline fun <reified T> Map<*, *>.get(key: String): T {
    return this[key] as T
}