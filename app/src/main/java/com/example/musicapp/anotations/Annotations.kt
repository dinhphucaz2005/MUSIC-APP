import androidx.compose.foundation.ExperimentalFoundationApi

/**
 * Annotation that marks a function or class as experimental in Jetpack Compose.
 *
 * This annotation is used to indicate that the related component or API
 * may not be stable and may change in the future. When using components
 * marked with this annotation, developers should be aware that they may
 * not work as expected or may change in future releases.
 *
 * @property ExperimentalLazyColumn
 *
 * ## Note
 * Using experimental components may require careful testing and validation
 * before integrating them into official source code.
 *
 * Refer to the official Jetpack Compose documentation for more information
 * on experimental APIs and how to use them safely.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalLazyColumn

