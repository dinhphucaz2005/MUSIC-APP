package nd.phuc.core.service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
//import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

private data class FileMetadata(
    val mimeType: String? = null,
    val acceptRange: String? = null,
    val contentLength: Long? = null
)


//@UnstableApi
//@AndroidEntryPoint
//class DownloadService : android.app.Service() {
//
//    private lateinit var downloadManager: DownloadManager
//
//    companion object {
//        const val URL = "url"
//        const val FILE_NAME = "file_name"
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    }
//
//    private suspend fun getFileMetadata(url: String): FileMetadata {
//        val client = HttpClient(OkHttp)
//
//        try {
//            val response: HttpResponse = client.head(url)
//
//            val mimeType = response.headers[HttpHeaders.ContentType]?.split(";")?.get(0)
//            val acceptRange = response.headers[HttpHeaders.AcceptRanges]
//            val contentLength = response.headers[HttpHeaders.ContentLength]
//
//            return FileMetadata(
//                mimeType = mimeType,
//                acceptRange = acceptRange,
//                contentLength = contentLength?.toLong()
//            )
//        } catch (e: Exception) {
//            println("Error: ${e.message}")
//            return FileMetadata()
//        } finally {
//            client.close()
//        }
//    }
//
//    private val contentTypesSupported =
//        listOf("audio/mpeg", "audio/mp3", "audio/webm", "application/pdf")
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        try {
//            val url = intent?.getStringExtra(URL)
//                ?: throw IllegalArgumentException("URL not provided")
//            val fileName = intent.getStringExtra(FILE_NAME)
//                ?: throw IllegalArgumentException("File name not provided")
//
//            val fileMetadata: FileMetadata = runBlocking(Dispatchers.IO) { getFileMetadata(url) }
//
//            if (fileMetadata.mimeType !in contentTypesSupported) {
//                throw IllegalArgumentException("Unsupported content type: ${fileMetadata.mimeType}")
//            }
//
//            val uri = Uri.parse(url)
//
//            val musicDirectory =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
//            val destinationUri =
//                Uri.withAppendedPath(
//                    Uri.fromFile(musicDirectory),
//                    "$fileName." + when (fileMetadata.mimeType) {
//                        "application/pdf" -> "pdf"
//                        else -> "mp3"
//                    }
//                )
//
//            val request = DownloadManager.Request(uri)
//                .setDestinationUri(destinationUri)
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setTitle("Downloading $fileName")
//                .setDescription("Downloading $fileName to Music folder")
//                .setMimeType(fileMetadata.mimeType)
//
//            val reference = downloadManager.enqueue(request)
//            Log.d("DownloadService", "Download started with reference: $reference")
//        } catch (e: Exception) {
//            Log.e("DownloadService", "Error while starting download: ${e.message}", e)
//        }
//
//        return START_NOT_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//}