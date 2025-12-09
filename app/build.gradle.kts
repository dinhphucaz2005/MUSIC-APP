import nd.phuc.musicapp.NiaBuildType

plugins {
    alias(libs.plugins.ndphuc.android.application)
    alias(libs.plugins.ndphuc.android.application.compose)
    alias(libs.plugins.ndphuc.android.application.flavors)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        applicationId = "nd.phuc.musicapp"
        versionCode = 1
        versionName = "1.0.0"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++2a"
                arguments += "-DANDROID_STL=c++_shared"
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = NiaBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            applicationIdSuffix = NiaBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.named("debug").get()
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    buildFeatures {
        prefab = true
    }

    namespace = "nd.phuc.musicapp"
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.flutter)
    implementation(libs.oboe)
}