package com.example.musicapp.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun withIOContext(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }

fun withMainContext(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }


