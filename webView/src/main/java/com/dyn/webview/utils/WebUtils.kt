package com.dyn.webview.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.blankj.utilcode.util.NetworkUtils

object WebUtils {
    fun isNetworkConnected(): Boolean {
        return NetworkUtils.isConnected()
    }
}