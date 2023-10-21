package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebView
import com.dyn.webview.jsbridge.CallBackFunction

/**
 * webView???? ???
 * */
interface WebCallback {
    //???'????
    fun onPageStarted(url: String?)
    //?????????
    fun onPageFinished(url: String?)
    //?????????
    fun onSmartPageError()
    //?????  ?l??????????
    fun onShowFileChooser(fileChooserParams: FileChooserParams?, filePathCallback: ValueCallback<Array<Uri>>?)
    /**
     * ???H5???js??k???
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?)
    fun onReceivedTitle(title: String)
}