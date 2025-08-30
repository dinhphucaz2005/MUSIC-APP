import org.gradle.internal.impldep.org.testng.remote.RemoteTestNG.isDebug

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.com.google.devtools.ksp)
//    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

val apiKey: String = System.getenv("API_KEY") ?: "SDLF"

//noinspection OldTargetApi
android {
    namespace = "nd.phuc.musicapp"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        applicationId = "nd.phuc.musicapp"
        minSdk = 26
        targetSdk = 34
        maxSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "nd.phuc.musicapp.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}


dependencies {

    // Media3
    implementation(libs.androidx.media)
    implementation(libs.bundles.media3)


    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.animation)

    // Core and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation for Compose
    implementation(libs.androidx.datastore.preferences.core.jvm)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Firebase
//    implementation(libs.firebase.database)
//    implementation(libs.firebase.storage)

    //Coil
    implementation(libs.bundles.coil)

    //ConstraintLayout
    implementation(libs.androidx.constraintlayout.compose)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Edit tag audio
    implementation(libs.mp3agic)

    //Color Picker
    implementation(libs.compose.color.picker.android)

    //Swipe Refresh
    implementation(libs.androidx.swiperefreshlayout)

    //Drag select
    implementation(libs.dragselect)
    implementation(libs.kotlinx.serialization.json)

    //Nav animation
    implementation(libs.accompanist.navigation.animation)

    //Extended Icon
    implementation(libs.fontawesomecompose)

    //Glance
//    implementation(libs.androidx.glance.appwidget)
//    implementation(libs.androidx.glance.material3)

    //Squiggly Slider
    implementation(libs.squigglyslider)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)


    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

//    implementation(libs.jtransforms)

    implementation(libs.androidx.documentfile)
    implementation(project(":flutter"))
}
