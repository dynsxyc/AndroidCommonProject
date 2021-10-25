package com.dyn.webview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView
import com.dyn.webview.remotewebview.javascriptinterface.WebViewJavaScriptInterface
import com.dyn.webview.remotewebview.settings.WebViewDefaultSettings
import com.dyn.webview.remotewebview.webchromeclient.DWebChromeClient
import com.dyn.webview.remotewebview.webviewclient.DWebViewClient
import com.dyn.webview.utils.WebConstants
import com.google.gson.Gson
import java.util.*

open class BaseWebView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : WebView(context, attrs, defStyleAttr), DWebViewClient.WebViewTouchListener {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    private var mWebCallback: WebCallback? = null
    var mHeader: Map<String, String>? = null //加载网页同步的请求头
    private val mRemoteInterface by lazy {
        val jsInterFace = WebViewJavaScriptInterface(getContext(),
            object : WebViewJavaScriptInterface.JavascriptCommand {
                override fun exec(context: Context, cmd: String, params: String?) {
                    val levle = when (cmd) {
                        WebConstants.COMMAND_ACTION.SHOWDIALOG,
                        WebConstants.COMMAND_ACTION.SHOWTOAST ->
                            WebConstants.LEVEL_LOCAL
                        else ->
                            WebConstants.LEVEL_BASE
                    }
                    mWebCallback?.exec(context, levle, cmd, params, this@BaseWebView)
                }

            })
        jsInterFace
    }

    fun registerWebViewCallBack(webCallback: WebCallback) {
        mWebCallback = webCallback
        webViewClient = DWebViewClient(this, mHeader, webCallback, this)
        webChromeClient = DWebChromeClient(webCallback)
        WebViewDefaultSettings.toSetting(this)
    }

    fun addJavascriptInterface(interfaceName: String){
        addJavascriptInterface(mRemoteInterface, interfaceName)
    }

    fun getWebViewCallBack(): WebCallback? {
        return mWebCallback
    }

    //native 通知js 事件回调
    fun dispatchEvent(name: String) {
        val param: MutableMap<String, String> = HashMap(1)
        param["name"] = name
        val trigger = "javascript:" + "dj.dispatchEvent" + "(" + Gson().toJson(param) + ")"
        callbackJs(trigger)
    }

    //native 调用js
    fun handleCallback(response: String) {
        if (!TextUtils.isEmpty(response)) {
            val trigger = "javascript:dj.callback($response)"
            callbackJs(trigger)
        }
    }

    /**
     * native 给js的回调
     * */
    private fun callbackJs(trigger: String) {
        try {
            evaluateJavascript(trigger, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun loadUrl(url: String) {
        if (mHeader == null) {
            super.loadUrl(url)
        } else {
            super.loadUrl(url, mHeader!!)
        }
        Log.e(Companion.TAG, "WebView load url : $url")
        resetAllStateInternal(url)

    }

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        super.loadUrl(url, additionalHttpHeaders)
        Log.e(Companion.TAG, "WebView load url : $url")
        resetAllStateInternal(url)
    }

    private fun resetAllStateInternal(url: String) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return
        }
        resetAllState()
    }

    // 加载url时重置touch状态
    private fun resetAllState() {
        isTouchByUser = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isTouchByUser = true
        }
        return super.onTouchEvent(event)
    }

    override var isTouchByUser = false

    companion object {
        private const val TAG = "BaseWebView"
    }
    /**
     * webView 点击图片 响应
     * */
//    将src作为参数传给java代码
//java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
// mWebView.addJavascriptInterface(new JsCallJavaObj() { @JavascriptInterface @Override public void showBigImg(String url) { Toast.makeText(mContext, "圖片路勁"+url, Toast.LENGTH_SHORT).show(); Intent intent = new Intent(mContext, BigImageActivity.class); intent.putExtra(Constants.IMG_URL,url); startActivity(intent); } },"jsCallJavaObj"); mWebView.setWebViewClient(new WebViewClient(){ @Override public void onPageFinished(WebView view, String url) { super.onPageFinished(view, url); setWebImageClick(view); } }); /** * 設置網頁中圖片的點擊事件 * @param view */ private void setWebImageClick(WebView view) { String jsCode="javascript:(function(){" + "var imgs=document.getElementsByTagName(\"img\");" + "for(var i=0;i<imgs.length;i++){" + "imgs[i].onclick=function(){" + "window.jsCallJavaObj.showBigImg(this.src);" + "}}})()"; mWebView.loadUrl(jsCode); } /** * Js調用Java接口 */ private interface JsCallJavaObj{ void showBigImg(String url); }
}