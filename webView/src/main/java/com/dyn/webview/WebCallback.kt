package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebView
import com.dyn.webview.jsbridge.CallBackFunction

/**
 * webView���� �ص�
 * */
interface WebCallback {
    //ҳ�濪ʼ����
    fun onPageStarted(url: String?)
    //ҳ����ؽ���
    fun onPageFinished(url: String?)
    //ҳ����ش���
    fun onSmartPageError()
    //��ҳ��  �ļ������ܴ���
    fun onShowFileChooser(fileChooserParams: FileChooserParams?, filePathCallback: ValueCallback<Array<Uri>>?)
    /**
     * ִ��H5�е�js�еķ���
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?)
    fun onReceivedTitle(title: String)
}