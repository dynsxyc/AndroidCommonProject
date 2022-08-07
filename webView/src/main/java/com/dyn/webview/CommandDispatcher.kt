package com.dyn.webview

import android.content.Context
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.command.webprocess.WebViewProcessCommandsManager
import com.dyn.webview.jsbridge.CallBackFunction
import com.dyn.webview.mainprocess.RemoteWebBinderPool
import com.dyn.webview.utils.MainLooper
import com.dyn.webview.utils.WebConstants
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.WebView

/**
 * 这个类主要用来处理子进程和主进程之间通信的
 * WebView所有请求分发
 * <p>
 * 规则：
 * <p>
 * 1、先处理UI依赖
 * 2、再判断是否是跨进程通信，跨进程通信需要通过AIDL方式分发数据
 * */
class CommandDispatcher {

    private var mMainProcessService: IWebToMain? = null //这个可以看做是绑定服务的copy
    /**
     * web端 进程的命令管理器
     * */
    private var mWebViewProcessCommandsManager:WebViewProcessCommandsManager = WebViewProcessCommandsManager.instance
    private val gSon by lazy {
        Gson()
    }
    private constructor()

    companion object {
        val instance by lazy {
            CommandDispatcher()
        }

        private const val TAG = "CommandDispatcher"
    }

    /**
     * 子进程绑定主进程服务
     * */
    fun initAidlConnect(context: Context) {
        if (mMainProcessService != null) {
            return
        }
        Thread {
            val iBinder = RemoteWebBinderPool.newInstance(context).queryBinder(RemoteWebBinderPool.BINDER_WEB_AIDL)
            mMainProcessService = IWebToMain.Stub.asInterface(iBinder)
            Logger.i("mWebAidlInterface------$iBinder------$mMainProcessService------")
        }.start()
    }

    /**
     * WebView进程开始  执行一条命令
     * js 调用native 方法
     *
     * */
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView, dispatcherCallBack: DispatcherCallBack?) {
        try {
            Logger.i( "exec: level->$commandLevel cmd->$cmd params->$params ")
            if (mWebViewProcessCommandsManager.checkHitLocalCommand(commandLevel, cmd)) {
                execLocalCommand(context, commandLevel, cmd, params, webView, dispatcherCallBack)
            } else {
                execRemoteCommand(commandLevel, cmd, params, webView, dispatcherCallBack)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
    fun exec(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?, dispatcherCallBack: DispatcherCallBack?) {
        try {
            Logger.i( "exec: level->$commandLevel cmd->$cmd params->$params ")
            if (mWebViewProcessCommandsManager.checkHitLocalCommand(commandLevel, cmd)) {
                execLocalCommand(context, commandLevel, cmd, params, callback, dispatcherCallBack)
            } else {
                execRemoteCommand(commandLevel, cmd, params, callback, dispatcherCallBack)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
    /**
     * 执行本地命令
     * 当前web进程的
     * */
    private fun execLocalCommand(context: Context, commandLevel: Int, cmd: String, params: String?, webView: WebView, dispatcherCallBack: CommandDispatcher.DispatcherCallBack?) {
        val mapParams = gSon.fromJson<Map<String,String>>(params,Map::class.java)
        mWebViewProcessCommandsManager.findAndExecCommand(context,commandLevel,cmd,mapParams,object : ResultBack {
            override fun onResult(status: Int, action: String?, data: Any) {
                try {
                    if (status == WebConstants.CONTINUE) {
                        execRemoteCommand(commandLevel, action, gSon.toJson(data), webView, dispatcherCallBack)
                    } else {
                        handleCallback(status, action, gSon.toJson(data), webView, dispatcherCallBack)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
    }
    private fun execLocalCommand(context: Context, commandLevel: Int, cmd: String, params: String?, callback: CallBackFunction?, dispatcherCallBack: CommandDispatcher.DispatcherCallBack?) {
        val mapParams = gSon.fromJson<Map<String,String>>(params,Map::class.java)
        mWebViewProcessCommandsManager.findAndExecCommand(context,commandLevel,cmd,mapParams,object : ResultBack {
            override fun onResult(status: Int, action: String?, data: Any) {
                try {
                    if (status == WebConstants.CONTINUE) {
                        execRemoteCommand(commandLevel, action, gSon.toJson(data), callback, dispatcherCallBack)
                    } else {
                        handleCallback(status, action, gSon.toJson(data), callback, dispatcherCallBack)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
    }

    /**
     * 跨进程命令的执行
     * */
    private fun execRemoteCommand(level: Int, action: String?, params: String?, webView: WebView, dispatcherCallBack: DispatcherCallBack?) {
        mMainProcessService?.handleWebAction(level,action,params,object :ICallbackFromMainToWeb.Stub(){
            override fun onResult(responseCode: Int, actionName: String?, response: String?) {
                handleCallback(responseCode, actionName!!, response!!, webView, dispatcherCallBack)
            }

        })
    }
    private fun execRemoteCommand(level: Int, action: String?, params: String?, callback: CallBackFunction?, dispatcherCallBack: DispatcherCallBack?) {
        mMainProcessService?.handleWebAction(level,action,params,object :ICallbackFromMainToWeb.Stub(){
            override fun onResult(responseCode: Int, actionName: String?, response: String?) {
                handleCallback(responseCode, actionName!!, response!!, callback, dispatcherCallBack)
            }

        })
    }

    /**
     * js 调用完native 后 native对js的回调
     * */
    private fun handleCallback(responseCode: Int,actionName: String?, response: String,
                               webView: WebView,dispatcherCallBack: DispatcherCallBack?) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response))

        MainLooper.runOnUiThread(Runnable {
            dispatcherCallBack?.preHandleBeforeCallback(responseCode, actionName, response)
            Logger.i("response ->$response")
            val params = gSon.fromJson<MutableMap<String, String>>(response, MutableMap::class.java)
            if (params.containsKey(WebConstants.NATIVE2WEB_CALLBACK) && params[WebConstants.NATIVE2WEB_CALLBACK].isNullOrEmpty().not()) {
                if (webView is BaseWebView) {
                    webView.handleCallback(response)
                }
            }
        })
    }
    private fun handleCallback(responseCode: Int,actionName: String?, response: String,
                               callback: CallBackFunction?,dispatcherCallBack: DispatcherCallBack?) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response))

        MainLooper.runOnUiThread(Runnable {
            dispatcherCallBack?.preHandleBeforeCallback(responseCode, actionName, response)
            Logger.i("response ->$response")
            val params = gSon.fromJson<MutableMap<String, String>>(response, MutableMap::class.java)
//            if (params.containsKey(WebConstants.NATIVE2WEB_CALLBACK) && params[WebConstants.NATIVE2WEB_CALLBACK].isNullOrEmpty().not()) {
                callback?.onCallBack(response)
//            }
        })
    }

    /**
     * Dispatcher 过程中的回调介入
     */
    interface DispatcherCallBack {
        fun preHandleBeforeCallback(responseCode: Int, actionName: String?, response: String?): Boolean
    }
}