package com.dyn.testwebview

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService
import com.orhanobut.logger.Logger

@AutoService(value = [Command::class])
class WebLoginCommand : Command {
    override fun name(): String {
        return WebConstants.CommandAction.APPLOGIN
    }

    override fun level(): Int {
        return WebConstants.LEVEL_BASE
    }

    override fun exec(context: Context, params: Map<String, String>?, resultBack: ResultBack?) {
        Logger.i("登录 调用 请求params->$params")
       resultBack?.onResult(1,name(), mutableMapOf("name" to "张三",WebConstants.NATIVE2WEB_CALLBACK to (params?.get(WebConstants.WEB2NATIVE_CALLBACk) ?: "")))
    }
}