plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "nd.phuc.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    // KSP
    ksp(libs.androidx.room.compiler)
    // Bundles(api)
    api(libs.bundles.media3)
    api(libs.bundles.core)
    api(libs.bundles.coil)
    api(libs.bundles.other)
    api(libs.bundles.navigation3)
    api(libs.bundles.lifecycle)
    api(libs.bundles.material3)
    api(libs.bundles.koin)
    // Bundles(implementation)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.room)
    // Libraries(api)
    api(libs.kotlinx.serialization.json)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui.tooling)
}