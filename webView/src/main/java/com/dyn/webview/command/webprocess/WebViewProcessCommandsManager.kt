package com.dyn.webview.command.webprocess

import android.content.Context
import android.util.Log
import com.dyn.base.utils.ServiceLoaderUtils
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.MainLooper
import com.dyn.webview.utils.WebConstants
import com.orhanobut.logger.Logger

/**
 * 命令管理器
 * */
class WebViewProcessCommandsManager {

    private constructor()

    companion object {
        val instance by lazy {
            WebViewProcessCommandsManager()
        }
        private val mLocalCommands= mutableMapOf<String,Command>()
    }
    init {
        MainLooper.runOnUiThread(Runnable {
            val services = ServiceLoaderUtils.loadServices(Command::class.java)
            Logger.i("注册命令->${services.hasNext()}")
            services.forEach {
                Logger.i("注册命令->${it.name()}")
                if (WebConstants.LEVEL_LOCAL == it.level()){
                    mLocalCommands[it.name()] = it
                }
            }
        })
    }


    /**
     * 寻找并执行命令
     * */
    fun findAndExecCommand(context: Context, level: Int, action: String, params: Map<String, Any>, resultBack: ResultBack) {
        mLocalCommands[action]?.exec(context, params, resultBack)
    }

    fun checkHitLocalCommand(level: Int, action: String): Boolean {
        return mLocalCommands.containsKey(action)
    }

}