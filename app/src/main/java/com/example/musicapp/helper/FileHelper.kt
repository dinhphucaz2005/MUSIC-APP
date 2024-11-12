package com.example.musicapp.helper

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

object FileHelper {

    fun getFileHash(file: File?): String? {
        file ?: return null
        val buffer = ByteArray(8192)
        val digest = MessageDigest.getInstance("SHA-256")

        FileInputStream(file).use { fis ->
            var bytesRead = fis.read(buffer)
            while (bytesRead != -1) {
                digest.update(buffer, 0, bytesRead)
                bytesRead = fis.read(buffer)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}