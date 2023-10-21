package com.dyn.webview.jsbridge

import android.content.Context
import android.os.Build
import android.os.Looper
import android.os.SystemClock
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.WebView

open class BridgeWebView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    WebView(context, attrs, defStyle), WebViewJavascriptBridge {
    var responseCallbacks: MutableMap<String?, CallBackFunction> = HashMap()
    var messageHandlers: MutableMap<String, BridgeHandler> = HashMap()
    var defaultHandler: BridgeHandler = DefaultHandler()

    private var startupMessage: MutableList<Message>? = ArrayList()

    fun getStartupMessage(): List<Message>? {
        return startupMessage
    }

    fun setStartupMessage(startupMessage: MutableList<Message>?) {
        this.startupMessage = startupMessage
    }

    private var uniqueId: Long = 0

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        this.isVerticalScrollBarEnabled = false
        this.isHorizontalScrollBarEnabled = false
        this.settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true)
        }
        this.webViewClient = generateBridgeWebViewClient()
    }

    private fun generateBridgeWebViewClient(): BridgeWebViewClient {
        return BridgeWebViewClient(this)
    }

    fun handlerReturnData(url: String) {
        val functionName = BridgeUtil.getFunctionFromReturnUrl(url)
        val f = responseCallbacks[functionName]
        val data = BridgeUtil.getDataFromReturnUrl(url)
        if (f != null) {
            f.onCallBack(data)
            responseCallbacks.remove(functionName)
            return
        }
    }
    override fun send(data: String?) {
        send(data, null)
    }

    override fun send(data: String?, responseCallback: CallBackFunction?) {
        doSend(null, data, responseCallback)
    }

    private fun doSend(handlerName: String?, data: String?, responseCallback: CallBackFunction?) {
        val m = Message()
        if (!TextUtils.isEmpty(data)) {
            m.data = data
        }
        if (responseCallback != null) {
            val callbackStr = String.format(
                BridgeUtil.CALLBACK_ID_FORMAT,
                (++uniqueId).toString() + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis())
            )
            responseCallbacks[callbackStr] = responseCallback
            m.callbackId = callbackStr
        }
        if (!TextUtils.isEmpty(handlerName)) {
            m.handlerName = handlerName
        }
        queueMessage(m)
    }

    private fun queueMessage(m: Message) {
        startupMessage?.add(m) ?: dispatchMessage(m)
    }

    fun dispatchMessage(m: Message) {
        var messageJson = m.toJson()
        //escape special characters for json string
        messageJson = messageJson?.replace("(\\\\)([^utrn])".toRegex(), "\\\\\\\\$1$2")
        messageJson = messageJson?.replace("(?<=[^\\\\])(\")".toRegex(), "\\\\\"")
        val javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson)
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            loadUrl(javascriptCommand)
        }
    }

    fun flushMessageQueue() {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            loadUrl(
                BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA,
                object : CallBackFunction {
                    override fun onCallBack(data: String?) {
                        // deserializeMessage
                        var list: List<Message>? = try {
                            Message.toArrayList(data)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return
                        }
                        if (list == null || list.isEmpty()) {
                            return
                        }
                        for (i in list.indices) {
                            val m = list[i]
                            val responseId = m.responseId
                            // 是否是response
                            if (!TextUtils.isEmpty(responseId)) {
                                val function = responseCallbacks[responseId]
                                val responseData = m.responseData
                                function!!.onCallBack(responseData)
                                responseCallbacks.remove(responseId)
                            } else {
                                var responseFunction: CallBackFunction? = null
                                // if had callbackId
                                val callbackId = m.callbackId
                                if (!TextUtils.isEmpty(callbackId)) {
                                    responseFunction = object : CallBackFunction {
                                        override fun onCallBack(data: String?) {
                                            val responseMsg = Message()
                                            responseMsg.responseId = callbackId
                                            responseMsg.responseData = data
                                            queueMessage(responseMsg)
                                        }
                                    }
                                } else {
                                    responseFunction = object : CallBackFunction {
                                        override fun onCallBack(data: String?) {
                                            // do nothing
                                        }
                                    }
                                }
                                var handler: BridgeHandler? =
                                    if (!TextUtils.isEmpty(m.handlerName)) {
                                        messageHandlers[m.handlerName]
                                    } else {
                                        defaultHandler
                                    }
                                handler?.handler(m.data, responseFunction)
                            }
                        }
                    }
                })
        }
    }

    private fun loadUrl(jsUrl: String, returnCallback: CallBackFunction) {
        loadUrl(jsUrl)
        responseCallbacks[BridgeUtil.parseFunctionName(jsUrl)] = returnCallback
    }

    /**
     * register handler,so that javascript can call it
     *
     * @param handlerName
     * @param handler
     */
    fun registerHandler(handlerName: String, handler: BridgeHandler) {
        messageHandlers[handlerName] = handler
    }

    /**
     * call javascript registered handler
     *
     * @param handlerName
     * @param data
     * @param callBack
     */
    fun callHandler(handlerName: String?, data: String?, callBack: CallBackFunction?) {
        doSend(handlerName, data, callBack)
    }
}