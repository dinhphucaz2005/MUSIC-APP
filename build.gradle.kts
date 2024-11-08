buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}


plugins {
    val kotlinVersion = "1.9.22"

    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.android.library") version "8.6.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
}