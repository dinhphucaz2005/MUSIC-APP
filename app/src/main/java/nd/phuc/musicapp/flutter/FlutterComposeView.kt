package nd.phuc.musicapp.flutter


import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

@Composable
fun FlutterComposeView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val fragmentManager = (context as FragmentActivity).supportFragmentManager
    val containerId = remember { View.generateViewId() }
    val fragment = remember { FlutterEngineHelper.createFlutterFragment() }

    var containerViewRef by remember { mutableStateOf<FragmentContainerView?>(null) }

    LaunchedEffect(containerViewRef) {
        containerViewRef?.let { container ->
            container.doOnPreDraw {
                if (!fragment.isAdded) {
                    fragmentManager.beginTransaction()
                        .replace(container.id, fragment)
                        .commit()
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                FragmentContainerView(ctx).apply {
                    id = containerId
                    containerViewRef = this
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
