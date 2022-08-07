package com.dyn.webview.utils

import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.dyn.webview.BaseWebView
import com.dyn.webview.DispatchWebEvent
import com.dyn.webview.WebCallback
import com.dyn.webview.jsbridge.BridgeHandler
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.WebView

object WebViewBindingAdapter {
    @BindingAdapter(value = ["loadWebUrl"], requireAll = false)
    @JvmStatic
    fun loadWebUrl(web: BaseWebView, url: String?) {
        url?.let {
            web.loadUrl(it)
        }
    }

    @BindingAdapter(value = ["webHeader"], requireAll = false)
    @JvmStatic
    fun webHeader(web: BaseWebView, webHeader: HashMap<String, String>?) {
        web.mHeader = webHeader
    }

    @BindingAdapter(value = ["loadDataWithBaseURL", "loadDataBaseurl"], requireAll = true)
    @JvmStatic
    fun loadDataWithBaseURL(web: BaseWebView, data: String?, baseUrl: String?) {
        data?.let {
            web.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null)
        }
    }

    @BindingAdapter(value = ["initWebView"], requireAll = false)
    @JvmStatic
    fun initWebView(web: BaseWebView, webCallback: WebCallback) {
        web.registerWebViewCallBack(webCallback)
    }

    @BindingAdapter(value = ["interfaceName"], requireAll = false)
    @JvmStatic
    fun addJavascriptInterface(web: BaseWebView, interfaceName: String) {
        web.addJavascriptInterface(interfaceName)
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

//    @InverseBindingAdapter(attribute = "canGoBack", event = "canGoBackStatus")
//    @JvmStatic
//    fun canGoBack(webView:WebView):Boolean{
//
//    }

}