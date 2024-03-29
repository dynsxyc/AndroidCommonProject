package com.dyn.webview.command.webprocess

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.dyn.webview.command.base.Command
import com.dyn.webview.command.base.ResultBack
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService

@AutoService(value = [Command::class])
class ShowDialogCommand : Command{
    override fun name(): String {
        return WebConstants.CommandAction.SHOWDIALOG
    }

    override fun exec(context: Context, params: Map<String, Any>?, resultBack: ResultBack?) {
        params?.let {
            if (it.isNotEmpty()) {
                val title = params["title"] as String?
                val content = params["content"] as String?
                var canceledOutside = 1
                if (params["canceledOutside"] != null) {
                    canceledOutside = (params["canceledOutside"] as Double).toInt()
                }
                val buttons = params["buttons"] as List<MutableMap<String, String?>>?
                val callbackName = params[WebConstants.WEB2NATIVE_CALLBACk]
                if (!TextUtils.isEmpty(content)) {
                    val dialog = AlertDialog.Builder(context!!)
                            .setTitle(title)
                            .setMessage(content)
                            .create()
                    dialog.setCanceledOnTouchOutside(canceledOutside == 1)
                    if (buttons.isNullOrEmpty().not()) {
                        for (i in buttons!!.indices) {
                            val resultData = buttons!![i]
                            val buttonWhich = getDialogButtonWhich(i)
                            if (buttonWhich == 0) return
                            dialog.setButton(buttonWhich, resultData["title"]) { dialog, which ->
                                resultData[WebConstants.NATIVE2WEB_CALLBACK] = callbackName as String?
                                resultBack?.onResult(WebConstants.SUCCESS, name(), resultData)
                            }
                        }
                    }
                    dialog.show()
                }
            }
        }
    }

    private fun getDialogButtonWhich(index: Int): Int {
        when (index) {
            0 -> return DialogInterface.BUTTON_POSITIVE
            1 -> return DialogInterface.BUTTON_NEGATIVE
            2 -> return DialogInterface.BUTTON_NEUTRAL
        }
        return 0
    }

    override fun level(): Int {
        return WebConstants.LEVEL_LOCAL
    }
}