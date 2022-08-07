package com.dyn.testwebview

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService

@AutoService(value = [Command::class])
class TestCommand : Command {
    override fun name(): String {
       return "testCmd"
    }

    override fun level(): Int {
        return WebConstants.LEVEL_LOCAL
    }

    override fun exec(context: Context, params: Map<String, String>?, resultBack: ResultBack?) {
        resultBack?.onResult(1,name(), mutableMapOf("name" to "你大哥"))
    }
}