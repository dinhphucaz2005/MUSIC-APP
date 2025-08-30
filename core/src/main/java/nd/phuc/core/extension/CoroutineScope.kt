package nd.phuc.core.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

fun withIOContext(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }

fun withMainContext(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }

// create database scope
val databaseScope = CoroutineScope(Dispatchers.IO) + Job()


