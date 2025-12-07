pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "My-Music-App"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core")
include(":presentation:music:android")
// Flutter module
include(":presentation")
apply(from = file("./presentation/.android/include_flutter.groovy"))
