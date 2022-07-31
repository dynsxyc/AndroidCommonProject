package com.github.jsbridge

interface BridgeHandler {
    fun handler(data: String?, function: CallBackFunction?)
}