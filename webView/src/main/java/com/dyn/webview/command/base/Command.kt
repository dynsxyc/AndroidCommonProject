package com.dyn.webview.command.base

import android.content.Context

/**
 * 抽象的命令层
 * 所有需要的命令都需要实现这个接口
 * */
interface Command {
    fun name(): String
    fun level():Int
    fun exec(context: Context,params: Map<String, String>?, resultBack: ResultBack?)
}