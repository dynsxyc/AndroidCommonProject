package com.umeng.lib

import android.content.Context
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

object UMManager {
    /**
     * 初始化
     * */
    fun init(context: Context) {
        /**
         * 注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
         */
        if (BuildConfig.IS_INIT_UMENG_STATISTICS) {
            UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null)
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
            MobclickAgent.setCatchUncaughtExceptions(true)
            UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        }
    }
}