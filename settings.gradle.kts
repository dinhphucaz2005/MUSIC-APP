pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://storage.googleapis.com/download.flutter.io")
    }
}

rootProject.name = "My Music App"
include(":app")
// Flutter
include(":third-party:aio_module")
project(":third-party:aio_module").projectDir = file("third-party/aio_module")
apply(from = file("third-party/aio_module/.android/include_flutter.groovy"))
include(":core")
