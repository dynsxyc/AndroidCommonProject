package com.github.lzyzsd.jsbridge

interface BridgeHandler {
    fun handler(data: String?, function: CallBackFunction?)
}