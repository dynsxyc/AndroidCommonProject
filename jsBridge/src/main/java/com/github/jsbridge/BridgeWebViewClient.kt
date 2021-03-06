package com.github.jsbridge

import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.jsbridge.BridgeHelper.Companion.BRIDGE_JS
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class BridgeWebViewClient(private val webView: BridgeWebView) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        var url = url
        try {
            url = URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url)
            true
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue()
            true
        } else {
            super.shouldOverrideUrlLoading(view, url)
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        BridgeUtil.webViewLoadLocalJs(view!!, BRIDGE_JS)

        if (webView.getStartupMessage() != null) {
            for (m in webView.getStartupMessage()!!) {
                webView.dispatchMessage(m)
            }
            webView.setStartupMessage(null)
        }
    }
}