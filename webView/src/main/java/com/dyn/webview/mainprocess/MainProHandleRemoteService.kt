package com.dyn.webview.mainprocess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
/**
 * 主进程的服务
 * */
class MainProHandleRemoteService : Service() {
    private val TAG = "MainProHandleRemoteServ"
    override fun onBind(intent: Intent?): IBinder? {
        val pid = android.os.Process.myPid()
        Log.d(TAG, String.format("$TAG:当前进程Id为$pid --- 客户端与服务端链接成功，服务端返回BinderPool.BinderPoolImpl对象"))
        return RemoteWebBinderPool.BinderPoolImpl(this)
    }
}