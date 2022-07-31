package com.dyn.webview.jsbridge

interface BridgeHandler {
    fun handler(data: String?, function: CallBackFunction?)
}