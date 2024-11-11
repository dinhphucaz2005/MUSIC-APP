plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val apiKey: String = System.getenv("API_KEY") ?: "SDLF"

//noinspection OldTargetApi
android {
    namespace = "com.example.musicapp"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        applicationId = "com.example.musicapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.example.musicapp.HiltTestRunner"
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
}

//noinspection GradleDependency
dependencies {

    // Inner Tube Module
    implementation(project(":innertube"))

    // Media3
    implementation("androidx.media:media:1.7.0")

    val media3Version = "1.3.1"

    // Warning: Update version of Media3. This may affect background playback functionality
    api("androidx.media:media:1.7.0")
    api("androidx.media3:media3-session:$media3Version")
    api("androidx.media3:media3-exoplayer:$media3Version")
    api("androidx.media3:media3-exoplayer-dash:$media3Version")
    api("androidx.media3:media3-ui:$media3Version")
    api("androidx.media3:media3-common:$media3Version")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.animation:animation:1.7.5")

    // Core and Lifecycle
    //noinspection GradleDependency
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.datastore:datastore-preferences-core-jvm:1.1.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest("com.google.dagger:hilt-compiler:2.51.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.51.1")
    androidTestImplementation("androidx.test:rules:1.6.1")


    //Room Database
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.1")

    //Firebase
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")

    //Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    //ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.8.3")

    //Edit tag audio
    implementation("com.mpatric:mp3agic:0.9.1")

    //EventBus
    implementation("org.greenrobot:eventbus:3.3.1")

    //Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Color Picker
    implementation("com.godaddy.android.colorpicker:compose-color-picker-android:0.7.0")

    //Swipe Refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.30.1")

    //Drag select
    implementation("com.dragselectcompose:dragselect:2.3.0")

    //Nav animation
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")


    //Serializable
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //Extended Icon
    implementation("com.github.Gurupreet:FontAwesomeCompose:1.1.0")

    //Glance
    implementation("androidx.glance:glance-appwidget:1.1.1")
    implementation("androidx.glance:glance-material3:1.1.1")
}