package com.github.lzyzsd.jsbridge

class DefaultHandler : BridgeHandler {
    override fun handler(data: String?, function: CallBackFunction?) {
        function?.onCallBack("DefaultHandler response data")
    }
}