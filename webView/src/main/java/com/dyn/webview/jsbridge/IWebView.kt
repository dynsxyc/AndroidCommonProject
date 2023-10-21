package com.dyn.webview.jsbridge

import android.content.Context
import android.webkit.ValueCallback

interface IWebView {
    fun getContext():Context
    fun loadUrl(url:String)
}