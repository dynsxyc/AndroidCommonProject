package com.dyn.webview.utils

object WebConstants {
    const val LOCAL_COMMAND_PARAMS = "command_params"
    const val LEVEL_LOCAL = 0 // 不需要跨进程的 local command, that is to say, this command execution does not require app.
    const val LEVEL_BASE = 1 // 需要跨进程的  基础level 涉及到账号相关的level

    const val CONTENT_SCHEME = "file:///android_asset/"

    const val SCHEME_SMS = "web_url"
    const val REQUEST_CODE_LOLIPOP = 1
    const val INTENT_TAG_URL = "web_url"
    const val INTENT_INTERFACE_NAME = "interface_name"
    const val INTENT_TAG_TITLE = "web_title"
    const val INTENT_TAG_HEADERS = "web_headers"
    const val WEB_IS_SHOW_ACTION_BAR = "web_is_show_action_bar"
    const val WEB_IS_SYNC_COOKIE = "web_is_sync_cookie"

    const val CONTINUE = 2 // 继续分发command
    const val SUCCESS = 1 // 成功
    const val FAILED = 0 // 失败
    const val EMPTY = "" // 无返回结果

    const val WEB2NATIVE_CALLBACk = "callback"
    const val NATIVE2WEB_CALLBACK = "callbackname"

    object ERRORCODE {
        const val NO_METHOD = -1000
        const val NO_AUTH = -1001
        const val NO_LOGIN = -1002
        const val ERROR_PARAM = -1003
        const val ERROR_EXCEPTION = -1004
    }

    object ERRORMESSAGE {
        const val NO_METHOD = "方法找不到"
        const val NO_AUTH = "方法权限不够"
        const val NO_LOGIN = "尚未登录"
        const val ERROR_PARAM = "参数错误"
        const val ERROR_EXCEPTION = "未知异常"
    }

    object COMMAND_ACTION {
        const val SHOWDIALOG = "showDialog"
        const val SHOWTOAST = "showToast"
        const val APPLOGIN = "appLogin"
        const val OPEN_PAGE = "start_activity"
    }
}