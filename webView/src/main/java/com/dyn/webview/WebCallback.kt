package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebView

interface WebCallback {
    fun pageStarted(url: String?)
    fun pageFinished(url: String?)
    fun onError()

    fun onShowFileChooser(cameraIntent: Intent?, filePathCallback: ValueCallback<Array<Uri>>?)

    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun onReceivedTitle(title: String)
}