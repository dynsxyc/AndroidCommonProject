package com.dyn.webview.remotewebview.webviewclient

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.webkit.*
import androidx.annotation.RequiresApi
import com.dyn.webview.R
import com.dyn.webview.WebCallback
import com.dyn.webview.utils.WebConstants

class DWebViewClient(
    private val mWebView: WebView,
    private val mHeader: Map<String, String>? = null,
    private val mWebCallback: WebCallback,
    private val mTouchListener: WebViewTouchListener
) : WebViewClient() {
    interface WebViewTouchListener {
        var isTouchByUser: Boolean
    }

    /**
     * url重定向会执行此方法 以及点击页面某些链接也会执行此方法
     * @return true:表示当前url已经加载完成，即使URL还会重定向都不会再进行加载
     *         false:表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     * */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        // 当前链接的重定向, 通过是否发生过点击行为来判断
        // 如果链接跟当前链接一样，表示刷新
        if (mTouchListener.isTouchByUser || mWebView.url == url) {
            return super.shouldOverrideUrlLoading(view, url)
        }
        if (url.isNullOrEmpty().not() && handleLinked(url)) {
            return true
        }
        // 控制页面中点开新的链接在当前webView中打开
        if (mHeader != null) {
            view?.loadUrl(url,mHeader)
        } else {
            view?.loadUrl(url)
        }
        return true
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // 当前链接的重定向
        if (mTouchListener.isTouchByUser) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        // 如果链接跟当前链接一样，表示刷新
        if (mWebView.url == request.url.toString()) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        if (handleLinked(request.url.toString())) {
            return true
        }
        // 控制页面中点开新的链接在当前webView中打开
        if (mHeader != null) {
            view.loadUrl(request.url.toString(),mHeader)
        } else {
            view.loadUrl(request.url.toString())
        }
        return true
    }

    /**
     * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
     */
    private fun handleLinked(url: String): Boolean {
        if (url.startsWith(WebView.SCHEME_TEL)
            || url.startsWith(WebView.SCHEME_MAILTO)
            || url.startsWith(WebView.SCHEME_GEO)
        ) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mWebView.context.startActivity(intent)
            } catch (ignored: ActivityNotFoundException) {
                ignored.printStackTrace()
            }
            return true
        }
        return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mWebCallback.pageStarted(url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mWebCallback.pageFinished(url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        mWebCallback.onError()
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
        val channel = ""
        if (!TextUtils.isEmpty(channel) && channel == "play.google.com") {
            val builder = AlertDialog.Builder(mWebView.context)
            var message = mWebView.context.getString(R.string.ssl_error)
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message =
                    mWebView.context.getString(R.string.ssl_error_not_trust)
                SslError.SSL_EXPIRED -> message =
                    mWebView.context.getString(R.string.ssl_error_expired)
                SslError.SSL_IDMISMATCH -> message =
                    mWebView.context.getString(R.string.ssl_error_mismatch)
                SslError.SSL_NOTYETVALID -> message =
                    mWebView.context.getString(R.string.ssl_error_not_valid)
            }
            message += mWebView.context.getString(R.string.ssl_error_continue_open)
            builder.setTitle(R.string.ssl_error)
            builder.setMessage(message)
            builder.setPositiveButton(
                R.string.continue_open,
                DialogInterface.OnClickListener { _, _ -> handler.proceed() })
            builder.setNegativeButton(
                R.string.cancel,
                DialogInterface.OnClickListener { _, _ -> handler.cancel() })
            val dialog = builder.create()
            dialog.show()
        } else {
            handler.proceed()
        }
    }


}