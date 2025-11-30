import com.android.build.api.dsl.TestExtension
import nd.phuc.musicapp.Android
import nd.phuc.musicapp.configureGradleManagedDevices
import nd.phuc.musicapp.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.test")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Android.TARGET_SDK
                configureGradleManagedDevices(this)
            }
        }
    }
}
