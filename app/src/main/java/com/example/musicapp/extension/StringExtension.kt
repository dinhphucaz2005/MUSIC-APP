package com.example.musicapp.extension

fun String.getFileNameExtension(): String {
    val index = this.lastIndexOf('.')
    return if (index == -1) "" else this.substring(index + 1)
}

fun String.getFileNameWithoutExtension(): String {
    val index = this.lastIndexOf('.')
    return if (index == -1) this else this.substring(0, index)
}