package com.github.jsbridge

class DefaultHandler : BridgeHandler {
    override fun handler(data: String?, function: CallBackFunction?) {
        function?.onCallBack("DefaultHandler response data")
    }
}