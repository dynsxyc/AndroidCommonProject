package com.dyn.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dyn.webview.jsbridge.CallBackFunction
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView

/**
 * webView���� �ص�
 * */
interface WebCallback {
    //ҳ�濪ʼ����
    fun onPageStarted(url: String?)
    //ҳ����ؽ���
    fun onPageFinished(url: String?)
    //ҳ����ش���
    fun onPageError()
    //��ҳ��  �ļ������ܴ���
    fun onShowFileChooser(choiceIntent: Intent?, filePathCallback: ValueCallback<Array<Uri>>?)
    /**
     * ִ��H5�е�js�еķ���
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView)
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?)
    fun onReceivedTitle(title: String)
}