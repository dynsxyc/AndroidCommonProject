package com.github.jsbridge

import android.webkit.WebView

interface OnWebViewClientListener  {
    fun shouldOverrideUrlLoading(view: WebView?, url: String):Boolean
    fun onPageFinished(view: WebView?, url: String?)
}