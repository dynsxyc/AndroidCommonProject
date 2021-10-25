package com.bugly.lib

import android.content.Context
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport

object BuglyApi {
    /**
     * @param context 上下文环境
     * @param packageName 包名
     * @param processName 进程名
     * @param appId bugLy APPID
     * @param isDebug 是否debug模式
     *  */
    private fun init(
        context: Context,
        packageName: String,
        processName: String,
        appId: String,
        isDebug: Boolean = false
    ) {
//         获取当前包名
//        val packageName: String = packageName
        // 获取当前进程名
//        val processName = ProcessUtils.getCurrentProcessName()
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
//         初始化Bugly
        Bugly.init(context, appId, isDebug)
    }
}