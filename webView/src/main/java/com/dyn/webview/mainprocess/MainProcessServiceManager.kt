package com.dyn.webview.mainprocess

import android.content.Context
import android.os.Process
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.dyn.webview.ICallbackFromMainToWeb
import com.dyn.webview.IWebToMain
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.command.mainprocess.MainProcessCommandsManager
import com.google.gson.Gson

/**
 * 主进程 提供给子进程服务实现
 * */
class MainProcessServiceManager private constructor(private val mContext: Context) : IWebToMain.Stub() {

    companion object {
        private var instance: MainProcessServiceManager? = null
        private val mMainProcessCommandsManager = MainProcessCommandsManager.instance
        fun getInstance(context: Context) = lazy {
            if (instance == null) {
                instance = MainProcessServiceManager(context)
            }
            instance
        }.value

        private const val TAG = "WetToMainManager"
    }
    /**
     * 子进程调用主进程的方法
     * */
    override fun handleWebAction(level: Int, actionName: String?, jsonParams: String?, callback: ICallbackFromMainToWeb?) {
        val pid = Process.myPid()
        Log.d(TAG, String.format("MainProAidlInterface: 进程ID（%d）， WebView请求（%s）, 参数 （%s）", pid, actionName, jsonParams))
        try {
            handleRemoteAction(level, actionName, Gson().fromJson<Map<String, String>>(jsonParams, MutableMap::class.java), callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleRemoteAction(level: Int, actionName: String?, paramMap: Map<String, String>, callback: ICallbackFromMainToWeb?) {
        mMainProcessCommandsManager.findAndExecRemoteCommand(mContext, level, actionName, paramMap, object : ResultBack {
            override fun onResult(status: Int, action: String?, result: Any) {
                try {
                    callback?.onResult(status, actionName, GsonUtils.toJson(result))
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}