package com.dyn.webview.utils

import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import com.dyn.webview.BaseWebView
import com.dyn.webview.DispatchWebEvent
import com.dyn.webview.WebCallback
import com.dyn.webview.jsbridge.BridgeHandler
import com.orhanobut.logger.Logger

object WebViewBindingAdapter {
    @BindingAdapter(value = ["loadWebUrl"], requireAll = false)
    @JvmStatic
    fun loadWebUrl(web: BaseWebView, url: String?) {
        url?.let {
            web.loadUrl(it)
        }
    }

    @BindingAdapter(value = ["loadDataWithBaseURL", "loadDataBaseurl"], requireAll = true)
    @JvmStatic
    fun loadDataWithBaseURL(web: BaseWebView, data: String?, baseUrl: String?) {
        data?.let {
            web.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null)
        }
    }

    @BindingAdapter(value = ["initWebView","webHeader"], requireAll = false)
    @JvmStatic
    fun initWebView(web: BaseWebView, webCallback: WebCallback?, header: HashMap<String, String>?) {
        webCallback?.let {
            web.requestFocus()
            web.isEnabled = true
            web.registerWebViewCallBack(it,header)
        }
    }

    @BindingAdapter(value = ["interfaceName"], requireAll = false)
    @JvmStatic
    fun addJavascriptInterface(web: BaseWebView, interfaceName: String?) {
        interfaceName?.let {
            web.addJavascriptInterface(it)
        }
    }
    @BindingAdapter(value = ["webLoadJsStr"], requireAll = false)
    @JvmStatic
    fun webLoadJsStr(web: BaseWebView, jsStr: String?) {
        jsStr?.let {
            if (Build.VERSION.SDK_INT< 18) {
                Logger.i("调用js方法 SDK_INT< 18  2-》$jsStr")
                web.loadUrl(jsStr)
            } else {
                Logger.i("调用js方法  3-》$jsStr")
                web.evaluateJavascript(jsStr) {
                    //返回JS方法中的返回值，我们没有写返回值所以为null
                }
            }
        }
    }
    @BindingAdapter(value = ["customInterfaceObject","customInterfaceName"], requireAll = true)
    @JvmStatic
    fun addJavascriptInterface(web: BaseWebView, interfaceObject: Any?, interfaceName: String?) {
        if (interfaceObject != null && TextUtils.isEmpty(interfaceName).not()){
            web.addJavascriptInterface(interfaceObject,interfaceName!!)
        }
    }

    @BindingAdapter(value = ["reloadWeb"])
    @JvmStatic
    fun reloadWeb(web: WebView, isReload: Boolean) {
        if (isReload) {
            web.reload()
        }
    }

    @BindingAdapter(value = ["dispatchEvent"])
    @JvmStatic
    fun dispatchEvent(web: BaseWebView, event: DispatchWebEvent?) {
        when (event) {
            DispatchWebEvent.ONRESUME -> {
                web.onResume()
                web.dispatchEvent("pageResume")
            }
            DispatchWebEvent.ONSTOP -> {
                web.dispatchEvent("pageStop")
            }
            DispatchWebEvent.ONPAUSE -> {
                web.onPause()
                web.dispatchEvent("pagePause")
            }
            DispatchWebEvent.ONDESTROYVIEW -> {
                web.dispatchEvent("pageDestroy")
            }
            else -> {}
        }
    }

    @BindingAdapter(value = ["clearWebView"])
    @JvmStatic
    fun clearWebView(mWebView: WebView, isClear: Boolean?) {
        if (isClear == true) {
            if (Looper.myLooper() != Looper.getMainLooper()) return
            mWebView.let { web ->
                web.stopLoading()
                web.handler?.let {
                    it.removeCallbacksAndMessages(null)
                }
                web.removeAllViews()
                (web.parent as ViewGroup?)?.let {
                    it.removeView(web)
                }
                web.webChromeClient = null
                web.tag = null
                web.clearHistory()
                web.destroy()
            }
        }
    }

    @BindingAdapter(value = ["canGoBackStatus", "backListener"], requireAll = false)
    @JvmStatic
    fun canGoBack(webView: WebView, canGoBackStatus: Boolean, backListener: OnBackListener?) {
        Logger.i("canGoBackStatus->$canGoBackStatus")
        if (canGoBackStatus) {
            if (webView.canGoBack()) {
                webView.goBack()
                backListener?.let {
                    it.onBack(true)
                }
            } else {
                backListener?.let {
                    it.onBack(false)
                }
            }
        }
    }

    @BindingAdapter(value = ["bridgeHandlerName", "bridgeHandler"], requireAll = true)
    @JvmStatic
    fun registerBridgeHandler(web: BaseWebView, handlerName: String?, handler: BridgeHandler?) {
        if (handlerName.isNullOrEmpty().not() && handler != null) {
            web.registerHandler(handlerName!!, handler)
        }
    }
    @BindingAdapter(value = ["bridgeCallHandler"])
    @JvmStatic
    fun callBridgeHandler(web: BaseWebView, handlerData: CallHandlerData?) {
        handlerData?.let {
            web.callHandler(it.handlerName,it.data,it.callBack)
        }
    }

//    @InverseBindingAdapter(attribute = "canGoBack", event = "canGoBackStatus")
//    @JvmStatic
//    fun canGoBack(webView:WebView):Boolean{
//
//    }

}