package nd.phuc.music.model


sealed class ThumbnailSource {

    fun getThumbnailUrl(): String? = if (this is FromUrl) url else null

    data class FromUrl(val url: String?) : ThumbnailSource()
    data class FromByteArray(val byteArray: ByteArray?) : ThumbnailSource() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FromByteArray

            return byteArray.contentEquals(other.byteArray)
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }

    data class FilePath(val path: String?) : ThumbnailSource()

    data object None : ThumbnailSource()
}