package com.dyn.webview.command.mainprocess

import android.content.Context
import android.util.Log
import com.dyn.base.utils.ServiceLoaderUtils
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.command.webprocess.WebViewProcessCommandsManager
import com.dyn.webview.utils.AidlError
import com.dyn.webview.utils.MainLooper
import com.dyn.webview.utils.WebConstants
import com.orhanobut.logger.Logger

/**
 *主进程命令管理器
 * */
class MainProcessCommandsManager {

    private constructor()

    companion object {
        private val accountLevelCommands= mutableMapOf<String,Command>()
        val instance by lazy {
            MainProcessCommandsManager()
        }
    }
    init {
        MainLooper.runOnUiThread(Runnable {
            val services = ServiceLoaderUtils.loadServices(Command::class.java)
            Logger.i("注册命令->${services.hasNext()}")
            services.forEach {
                Logger.i("注册命令->${it.name()}")
                if (WebConstants.LEVEL_BASE == it.level()){
                    accountLevelCommands[it.name()] = it
                }
            }
        })
    }

    /**
     * 非UI Command 的执行
     */
    fun findAndExecRemoteCommand(context: Context, level: Int, action: String?, params: Map<String, String>?, resultBack: ResultBack) {
        var methodFlag = false
        if (accountLevelCommands.containsKey(action)) {
            methodFlag = true
            accountLevelCommands[action]?.exec(context, params, resultBack)
        }
        if (!methodFlag) {
            val aidlError = AidlError(WebConstants.ERRORCODE.NO_METHOD, WebConstants.ERRORMESSAGE.NO_METHOD)
            resultBack.onResult(WebConstants.FAILED, action, aidlError)
        }
    }

}