package com.dyn.webview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.ValueCallback
import android.webkit.WebView
import com.dyn.webview.jsbridge.*
import com.dyn.webview.remotewebview.javascriptinterface.WebViewJavaScriptInterface
import com.dyn.webview.remotewebview.settings.WebViewDefaultSettings
import com.dyn.webview.remotewebview.webchromeclient.DWebChromeClient
import com.dyn.webview.remotewebview.webviewclient.DWebViewClient
import com.dyn.webview.utils.WebConstants
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import java.util.Objects

open class BaseWebView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,defStyleRes:Int
) : WebView(context, attrs, defStyleAttr,defStyleRes), DWebViewClient.WebViewTouchListener,WebViewJavascriptBridge, IWebView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?,defStyleAttr: Int) : this(context, attrs, android.R.attr.webViewStyle,0)

    private var mWebCallback: WebCallback? = null
    private var mHeader: Map<String, String>? = null //加载网页同步的请求头
    private val mRemoteInterface by lazy {
        val jsInterFace = WebViewJavaScriptInterface(getContext(),
            object : WebViewJavaScriptInterface.JavascriptCommand {
                override fun exec(context: Context, cmd: String, params: String?) {
                    val level = when (cmd) {
                        WebConstants.CommandAction.SHOWDIALOG,
                        WebConstants.CommandAction.SHOWTOAST ->
                            WebConstants.LEVEL_LOCAL
                        else ->
                            WebConstants.LEVEL_BASE
                    }
                    mWebCallback?.exec(context, level, cmd, params, this@BaseWebView)
                }

            })
        jsInterFace
    }

    private var bridgeHelper: BridgeHelper

    init {
        WebViewDefaultSettings.toSetting(this)
        bridgeHelper = BridgeHelper(this)
    }

    fun registerWebViewCallBack(webCallback: WebCallback, header: HashMap<String, String>?) {
        mHeader = header
        mWebCallback = webCallback
        webViewClient = DWebViewClient(this, header, webCallback, bridgeHelper, this)
        webChromeClient = DWebChromeClient(webCallback)

//        callHandler("functionInJs", Gson().toJson(mutableMapOf("name" to "张三")), object :
//            CallBackFunction {
//            override fun onCallBack(data: String?) {
//                Log.d(TAG, "onCallBack: $data")
//            }
//        })
//
//        registerHandler("submitFromWeb", handler = object :BridgeHandler{
//            override fun handler(data: String?, function: CallBackFunction?) {
//                Log.d(TAG, "handler: $data")
//                val level = when (cmd) {
//                    WebConstants.CommandAction.SHOWDIALOG,
//                    WebConstants.CommandAction.SHOWTOAST ->
//                        WebConstants.LEVEL_LOCAL
//                    else ->
//                        WebConstants.LEVEL_BASE
//                }
//                mWebCallback?.exec(context, level, cmd, params, function)
//            }
//        })
//
//        send("hello")

    }

    fun addJavascriptInterface(interfaceName: String) {
        addJavascriptInterface(mRemoteInterface, interfaceName)
    }

    fun getWebViewCallBack(): WebCallback? {
        return mWebCallback
    }

    /**
     * 给H5 页面分发native生命周期事件
     * */
    fun dispatchEvent(name: String) {
        val param: MutableMap<String, String> = HashMap(1)
        param["name"] = name
        val trigger = "javascript:" + "dj.dispatchEvent" + "(" + Gson().toJson(param) + ")"
        callbackJs(trigger)
    }

    /**
     * JavaScript请求完native方法后  native方法对js的回调
     * */
    fun handleCallback(response: String) {
        if (!TextUtils.isEmpty(response)) {
            val trigger = "javascript:jieshou(${response})"
            callbackJs(trigger)
        }
    }

    /**
     * 执行JavaScript方法   给H5的回调
     * */
    private fun callbackJs(trigger: String) {
        try {
            evaluateJavascript(trigger) {
                Logger.i("js 方法返回->$it")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun evaluateJavascript(script: String, resultCallback: ValueCallback<String>?) {
        super.evaluateJavascript(script, resultCallback)
    }

    override fun loadUrl(url: String) {
        if (mHeader == null) {
            super.loadUrl(url)
        } else {
            super.loadUrl(url, mHeader!!)
        }
        Logger.i( "WebView load url 1: $url")
        resetAllStateInternal(url)

    }

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        super.loadUrl(url, additionalHttpHeaders)
        Logger.i( "WebView load url 2: $url")
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

    /**
     * webView 点击图片 响应
     * */
//    将src作为参数传给java代码
//java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
// mWebView.addJavascriptInterface(new JsCallJavaObj() { @JavascriptInterface @Override public void showBigImg(String url) { Toast.makeText(mContext, "圖片路勁"+url, Toast.LENGTH_SHORT).show(); Intent intent = new Intent(mContext, BigImageActivity.class); intent.putExtra(Constants.IMG_URL,url); startActivity(intent); } },"jsCallJavaObj"); mWebView.setWebViewClient(new WebViewClient(){ @Override public void onPageFinished(WebView view, String url) { super.onPageFinished(view, url); setWebImageClick(view); } }); /** * 設置網頁中圖片的點擊事件 * @param view */ private void setWebImageClick(WebView view) { String jsCode="javascript:(function(){" + "var imgs=document.getElementsByTagName(\"img\");" + "for(var i=0;i<imgs.length;i++){" + "imgs[i].onclick=function(){" + "window.jsCallJavaObj.showBigImg(this.src);" + "}}})()"; mWebView.loadUrl(jsCode); } /** * Js調用Java接口 */ private interface JsCallJavaObj{ void showBigImg(String url); }


    /******************jsBridge   start **************************/

    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     *                if js message has handler name, it will be handled by named handlers registered by native
     */
    fun setDefaultHandler(handler: BridgeHandler) {
        bridgeHelper.defaultHandler = handler
    }

    override fun send(data: String?) {
        send(data, null);
    }

    override fun send(data: String?, responseCallback: CallBackFunction?) {
        bridgeHelper.send(data, responseCallback);
    }


    /**
     * register handler,so that javascript can call it
     * 注册处理程序,以便javascript调用它
     *
     * @param handlerName handlerName
     * @param handler     BridgeHandler
     */
    fun registerHandler(handlerName: String, handler: BridgeHandler) {
        bridgeHelper.registerHandler(handlerName, handler);
    }

    /**
     * unregister handler
     *
     * @param handlerName
     */
    public fun unregisterHandler(handlerName: String?) {
        bridgeHelper.unregisterHandler(handlerName);
    }

    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     *
     * @param handlerName handlerName
     * @param data        data
     * @param callBack    CallBackFunction
     */
    fun callHandler(handlerName: String?, data: String?, callBack: CallBackFunction?) {
        bridgeHelper.callHandler(handlerName, data, callBack);
    }

    /******************jsBridge   end   **************************/

}