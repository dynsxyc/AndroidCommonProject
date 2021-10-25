package com.dyn.webview.command.base
/**
 * native给js 的回调
 * */
interface ResultBack {
    /**
     * @param data 数据不需要toJson 传一个对象进程间通信的时候会自动toJson
     * */
    fun onResult(status:Int,action:String?,data:Any)
}