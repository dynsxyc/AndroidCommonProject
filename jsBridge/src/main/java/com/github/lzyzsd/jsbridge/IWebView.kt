package com.github.lzyzsd.jsbridge

import android.content.Context

interface IWebView {
    fun getContext():Context
    fun loadUrl(url:String)
}