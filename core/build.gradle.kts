plugins {
    alias(libs.plugins.ndphuc.android.library)
    alias(libs.plugins.ndphuc.android.room)
    alias(libs.plugins.ndphuc.android.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "nd.phuc.core"
}

room {
    schemaDirectory("schemas")
}

dependencies {
    api(libs.androidx.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    api(libs.androidx.core.ktx)
    api(libs.androidx.animation)
    api(libs.androidx.constraintlayout.compose)
    api(libs.timber)
    api(libs.bundles.media3)
    api(libs.bundles.coil)
    api(libs.bundles.koin)
    runtimeOnly(libs.koin.compose)
    api(libs.bundles.ktor)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.viewmodel.ktx)
}