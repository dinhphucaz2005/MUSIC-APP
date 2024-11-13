package com.example.musicapp.ui.screen.setting

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.innertube.YouTube
import com.example.musicapp.R
import com.example.musicapp.extension.logYT
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.viewmodels.LoginViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@DelicateCoroutinesApi
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var webView: WebView? = null

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            CommonIcon(icon = R.drawable.ic_back) { navController.popBackStack() }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun doUpdateVisitedHistory(
                            view: WebView,
                            url: String,
                            isReload: Boolean
                        ) {
                            if (url.startsWith("https://music.youtube.com")) {
                                val innerTubeCookie = CookieManager.getInstance().getCookie(url)
                                viewModel.saveCookie(innerTubeCookie)
//                                GlobalScope.launch {
//                                    YouTube.accountInfo().onSuccess {
//                                        accountName = it.name
//                                        accountEmail = it.email.orEmpty()
//                                        accountChannelHandle = it.channelHandle.orEmpty()
//                                        logYT("doUpdateVisitedHistory: $it")
//                                    }.onFailure {
//                                        it.printStackTrace()
//                                    }
//                                }
                            }
                        }

                        override fun onPageFinished(view: WebView, url: String?) {
                            loadUrl("javascript:Android.onRetrieveVisitorData(window.yt.config_.VISITOR_DATA)")
                        }
                    }
                    settings.apply {
                        javaScriptEnabled = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                    }
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onRetrieveVisitorData(newVisitorData: String?) {
                            if (newVisitorData != null) {
                                viewModel.saveVisitorData(newVisitorData)
                            }
                        }
                    }, "Android")
                    webView = this
                    loadUrl("https://accounts.google.com/ServiceLogin?ltmpl=music&service=youtube&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26next%3Dhttps%253A%252F%252Fmusic.youtube.com%252F")
                }
            }
        )
    }

    BackHandler(enabled = webView?.canGoBack() == true) {
        webView?.goBack()
    }
}