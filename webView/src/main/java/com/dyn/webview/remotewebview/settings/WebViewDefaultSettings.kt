package com.dyn.webview.remotewebview.settings

import android.os.Build
import com.dyn.webview.utils.WebUtils
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

/**
 *
 * webView 默认设置 进行简单配置
 * */
object WebViewDefaultSettings {
    fun toSetting(webView: WebView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.enableSlowWholeDocumentDraw()
        }
        val mWebSettings = webView.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        mWebSettings.setSupportZoom(true)
        mWebSettings.builtInZoomControls = true
        if (WebUtils.isNetworkConnected()) {
            mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            mWebSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 硬件加速兼容性问题有点多
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        }

//        mWebSettings.textZoom = 100
        mWebSettings.databaseEnabled = true
        mWebSettings.setAppCacheEnabled(true)
        mWebSettings.loadsImagesAutomatically = true
//        mWebSettings.setSupportMultipleWindows(false)
        mWebSettings.blockNetworkImage = false//是否阻塞加载网络图片  协议http or https
        mWebSettings.allowFileAccess = true //允许加载本地文件html  file协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebSettings.setAllowFileAccessFromFileURLs(true) //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            mWebSettings.setAllowUniversalAccessFromFileURLs(true)//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        }
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        } else {
//            mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
//        }
        mWebSettings.savePassword = false
        mWebSettings.saveFormData = false
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.setNeedInitialFocus(true)
//        mWebSettings.defaultTextEncodingName = "utf-8"//设置编码格式
//        mWebSettings.defaultFontSize = 16
//        mWebSettings.minimumFontSize = 10//设置 WebView 支持的最小字体大小，默认为 8
        mWebSettings.setGeolocationEnabled(true)

//        val appCacheDir = webView.context.getDir("cache", Context.MODE_PRIVATE).path
//        Logger.i( "WebView cache dir: $appCacheDir")
//        mWebSettings.databasePath = appCacheDir
//        mWebSettings.setAppCachePath(appCacheDir)
//        mWebSettings.setAppCacheMaxSize(1024*1024*80)

        // 用户可以自己设置useragent
//        mWebSettings.userAgentString = "webprogress/build you agent info"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
    }

}