package com.dyn.webview.command.webprocess

import android.content.Context
import android.widget.Toast
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService

@AutoService(value = [Command::class])
class ShowToastCommand : Command {
    override fun name(): String {
        return WebConstants.CommandAction.SHOWTOAST
    }

    override fun exec(context: Context, params: Map<String, Any>?, resultBack: ResultBack?) {
        params?.let {
            Toast.makeText(context, it["message"].toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun level(): Int {
        return WebConstants.LEVEL_LOCAL
    }
}