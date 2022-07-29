package com.dyn.testwebview

import android.content.ComponentName
import android.content.Intent
import com.dyn.base.ui.base.BaseApplication
import com.dyn.webview.command.base.OptimizationService
import com.dyn.webview.utils.WebConstants
import com.kingja.loadsir.LoadSirUtil
import com.kingja.loadsir.core.LoadSir

class TestApp :BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        val intent = Intent()
        intent.component = ComponentName(this,OptimizationService::class.java)
        intent.putExtra(WebConstants.INTENT_TAG_URL,"https://xw.qq.com/?f=qqcom")
        startService(intent)
        LoadSir.beginBuilder()
    }
}