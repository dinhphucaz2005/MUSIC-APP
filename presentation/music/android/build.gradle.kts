plugins {
    id("com.android.library")
    kotlin("android")
}

group = "nd.phuc.music"
version = "1.0"

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

android {
    namespace = "nd.phuc.music"
    compileSdk = libs.versions.complie.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = libs.versions.java.version.get().let { JavaVersion.toVersion(it) }
        targetCompatibility = libs.versions.java.version.get().let { JavaVersion.toVersion(it) }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

}

dependencies {
    implementation(libs.bundles.media3)
}
