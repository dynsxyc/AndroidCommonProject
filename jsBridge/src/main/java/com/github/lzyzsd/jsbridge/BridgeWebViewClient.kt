package com.github.lzyzsd.jsbridge

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
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
        BridgeUtil.webViewLoadLocalJs(view!!, BridgeUtil.toLoadJs)

        if (webView.getStartupMessage() != null) {
            for (m in webView.getStartupMessage()!!) {
                webView.dispatchMessage(m)
            }
            webView.setStartupMessage(null)
        }
    }
}