package com.dyn.webview.utils

import com.dyn.webview.jsbridge.CallBackFunction

data class CallHandlerData(val handlerName:String,val data:String,val callBack: CallBackFunction? = null)
