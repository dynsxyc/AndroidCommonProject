package com.dyn.webview.jsbridge

import com.dyn.webview.jsbridge.BridgeHandler
import com.dyn.webview.jsbridge.CallBackFunction

class DefaultHandler : BridgeHandler {
    override fun handler(data: String?, function: CallBackFunction?) {
        function?.onCallBack("DefaultHandler response data")
    }
}