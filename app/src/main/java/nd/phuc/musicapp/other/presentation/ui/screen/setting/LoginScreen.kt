package nd.phuc.musicapp.other.presentation.ui.screen.setting

//import android.annotation.SuppressLint
//import android.webkit.CookieManager
//import android.webkit.JavascriptInterface
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import nd.phuc.musicapp.R
//import nd.phuc.musicapp.core.presentation.components.CommonIcon
//import nd.phuc.musicapp.other.viewmodels.LoginViewModel
//
//@SuppressLint("SetJavaScriptEnabled")
//@Composable
//fun LoginScreen(
//    navController: NavHostController,
//    viewModel: LoginViewModel = hiltViewModel(),
//    reload: () -> Unit
//) {
//
//    var webView: WebView? = null
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Row {
//            CommonIcon(icon = R.drawable.ic_back) { navController.popBackStack() }
//        }
//        AndroidView(
//            modifier = Modifier
//                .fillMaxSize(),
//            factory = { context ->
//                WebView(context).apply {
//                    webViewClient = object : WebViewClient() {
//                        override fun doUpdateVisitedHistory(
//                            view: WebView,
//                            url: String,
//                            isReload: Boolean
//                        ) {
//                            if (url.startsWith("https://music.youtube.com")) {
//                                val innerTubeCookie = CookieManager.getInstance().getCookie(url)
//                                if (innerTubeCookie != null) {
//                                    viewModel.saveCookie(innerTubeCookie)
//                                    navController.popBackStack()
//                                }
//                            }
//                        }
//
//                        override fun onPageFinished(view: WebView, url: String?) {
//                            loadUrl("javascript:Android.onRetrieveVisitorData(window.yt.config_.VISITOR_DATA)")
//                        }
//                    }
//                    settings.apply {
//                        javaScriptEnabled = true
//                        setSupportZoom(true)
//                        builtInZoomControls = true
//                    }
//                    addJavascriptInterface(object {
//                        @JavascriptInterface
//                        fun onRetrieveVisitorData(newVisitorData: String?) {
//                            if (newVisitorData != null) {
//                                viewModel.saveVisitorData(newVisitorData)
//                                reload()
//                            }
//                        }
//                    }, "Android")
//                    webView = this
//                    loadUrl("https://accounts.google.com/ServiceLogin?ltmpl=music&service=youtube&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26next%3Dhttps%253A%252F%252Fmusic.youtube.com%252F")
//                }
//            }
//        )
//    }
//
//    BackHandler(enabled = webView?.canGoBack() == true) {
//        webView?.goBack()
//    }
//}