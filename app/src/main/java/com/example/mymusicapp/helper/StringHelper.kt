package com.example.mymusicapp.helper

import com.example.mymusicapp.common.AppCommon
import java.util.Locale

object StringHelper {
    fun convert(input: String): String {
        return input.lowercase(Locale.ROOT).replace(Regex("[àáạảãâầấậẩẫăằắặẳẵ]"), "a")
            .replace(Regex("[èéẹẻẽêềếệểễ]"), "e")
            .replace(Regex("[ìíịỉĩ]"), "i")
            .replace(Regex("[òóọỏõôồốộổỗơờớợởỡ]"), "o")
            .replace(Regex("[ùúụủũưừứựửữ]"), "u")
            .replace(Regex("[ỳýỵỷỹ]"), "y")
            .replace(Regex("đ"), "d")
    }

    fun getHash(key: String): Long {
        val newKey = convert(key)
        var value: Long = 0
        newKey.forEach {
            val asciiCode = it.code
            if (asciiCode <= 255)
                value = (value * AppCommon.BASE + asciiCode) % AppCommon.MOD
        }
        return value
    }

    fun matching(fileName: String, searchQuery: String): Boolean {
        if (searchQuery == "")
            return true
        val newFileName = convert(fileName)
        val newSearchQuery = convert(searchQuery)
        val a = newFileName.replace("\\s+".toRegex(), " ").trim().split(" ")
        val b = newSearchQuery.replace("\\s+".toRegex(), " ").trim().split(" ")
        var i = 0
        var j = 0
        while (i < a.size && j < b.size) {
            if (a[i] == b[j]) {
                i++
                j++
            } else {
                i++
            }
        }
        return j == b.size
    }
}