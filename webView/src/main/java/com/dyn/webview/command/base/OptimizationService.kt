package com.dyn.webview.command.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dyn.webview.BaseWebView
import com.dyn.webview.utils.WebConstants
import com.orhanobut.logger.Logger

/**
 * webView 进程的service
 * 这个进程主要是做webView加载慢  优化用的
 * 在App启动的时候启动这个service 在app调用web页面的时候可以提高webView的加载速度  相当于一次预加载
 * */
class OptimizationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(WebConstants.INTENT_TAG_URL)
        Logger.i("onStartCommand---------------$url")
        url?.let {
            BaseWebView(this).loadUrl(url)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }
}