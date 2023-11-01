package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebView
import com.dyn.webview.jsbridge.CallBackFunction

/**
 * webView监听 回调
 * */
interface WebCallback {
    //页面开始加载
    fun onPageStarted(url: String?)
    //页面加载结束
    fun onPageFinished(url: String?)
    //页面加载错误
    fun onSmartPageError()
    //网页中  文件请求功能触发
    fun onShowFileChooser(fileChooserParams: FileChooserParams?, filePathCallback: ValueCallback<Array<Uri>>?)
    /**
     * 执行H5中的js中的方法
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?)
    fun onReceivedTitle(title: String)
}