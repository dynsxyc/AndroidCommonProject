package com.dyn.webview.utils

import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.dyn.webview.BaseWebView
import com.dyn.webview.DispatchWebEvent
import com.dyn.webview.WebCallback
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.WebView

object WebViewBindingAdapter {
    @BindingAdapter(value = ["loadWebUrl", "webHeader"], requireAll = false)
    @JvmStatic
    fun loadWebUrl(web: BaseWebView, url: String?, webHeader: HashMap<String, String>?) {
        web.mHeader = webHeader
        url?.let {
            web.loadUrl(it)
        }
    }
    @BindingAdapter(value = ["loadDataWithBaseURL"], requireAll = false)
    @JvmStatic
    fun loadDataWithBaseURL(web: BaseWebView, data: String?) {
        data?.let {
            web.loadDataWithBaseURL(null,data,"text/html", "utf-8", null)
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
                clearWebView(web)
                web.dispatchEvent("pageDestroy")
            }
            else -> {}
        }
    }

    private fun clearWebView(mWebView: WebView) {
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

//    @InverseBindingAdapter(attribute = "canGoBack", event = "canGoBackStatus")
//    @JvmStatic
//    fun canGoBack(webView:WebView):Boolean{
//
//    }

}