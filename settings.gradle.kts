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
include(":core")
/*
// Flutter
include(":third-party:aio_module")
project(":third-party:aio_module").projectDir = file("third-party/aio_module")
val preSyncScript = file("pre_sync.sh")
if (!preSyncScript.exists()) {
    throw GradleException("pre_sync.sh not found! Make sure the file exists.")
}
println("Running pre_sync.sh...")
exec {
    commandLine("bash", preSyncScript.absolutePath)
}
println("pre_sync.sh finished.")
apply(from = file("third-party/aio_module/.android/include_flutter.groovy"))
*/
