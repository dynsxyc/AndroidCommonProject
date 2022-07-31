package com.dyn.webview.jsbridge

import com.dyn.webview.jsbridge.CallBackFunction

interface WebViewJavascriptBridge {
    fun send(data: String?)
    fun send(data: String?, responseCallback: CallBackFunction?)
}