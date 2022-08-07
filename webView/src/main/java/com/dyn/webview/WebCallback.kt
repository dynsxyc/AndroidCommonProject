package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dyn.webview.jsbridge.CallBackFunction
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView

/**
 * webView监听 回调
 * */
interface WebCallback {
    //页面开始加载
    fun onPageStarted(url: String?)
    //页面加载接收
    fun onPageFinished(url: String?)
    //页面加载错误
    fun onPageError()
    //网页中  文件请求功能触发
    fun onShowFileChooser(choiceIntent: Intent?, filePathCallback: ValueCallback<Array<Uri>>?)
    /**
     * 执行H5中的js中的方法
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?)
    fun onReceivedTitle(title: String)
}