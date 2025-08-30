package nd.phuc.core.extension

import java.io.File

fun File.getFileId(): String {
    val string = absolutePath + lastModified().toString()
    val base = 311
    val mod = 1_000_100_007
    var hash = 0L
    string.forEach { char -> hash = (hash * base + (char.code + 1)) % mod }
    return hash.toString()
}