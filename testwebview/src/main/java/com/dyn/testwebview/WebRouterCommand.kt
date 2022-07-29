package com.dyn.testwebview

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService

@AutoService(value = [Command::class])
class WebRouterCommand : Command {
    override fun name(): String {
        return WebConstants.CommandAction.OPEN_PAGE
    }

    override fun level(): Int {
        return WebConstants.LEVEL_BASE
    }

    override fun exec(context: Context, params: Map<String, String>?, resultBack: ResultBack?) {
        val nav = params?.get("arouter_navigation")
        val intent = Intent()
        nav?.let {
            intent.component = ComponentName(context, nav)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}