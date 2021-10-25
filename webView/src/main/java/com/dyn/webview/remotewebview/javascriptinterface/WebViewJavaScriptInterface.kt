package com.dyn.webview.remotewebview.javascriptinterface

import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface

class WebViewJavaScriptInterface(private val mContext: Context,private val mJavascriptCommand:JavascriptCommand) {
    private val mHandler by lazy {
        Handler()
    }
    @JavascriptInterface
    open fun post(cmd: String?, param: String?) {
        if (cmd.isNullOrEmpty()){
            return
        }
        mHandler.post(Runnable {
            try {
                mJavascriptCommand.exec(mContext, cmd, param)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    open interface JavascriptCommand {
        fun exec(context: Context, cmd: String, params: String?)
    }
}