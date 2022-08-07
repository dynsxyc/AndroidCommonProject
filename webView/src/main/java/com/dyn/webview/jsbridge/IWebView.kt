package com.dyn.webview.jsbridge

import android.content.Context
import com.tencent.smtt.sdk.ValueCallback

interface IWebView {
    fun getContext():Context
    fun loadUrl(url:String)
    fun evaluateJavascript(url:String,var2: ValueCallback<String>?)
}