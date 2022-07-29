package com.dyn.testwebview

import com.blankj.utilcode.util.GsonUtils
import com.dyn.base.ui.base.BaseApplication
import com.dyn.base.utils.PreferenceExt
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig

object TestPreference {
    var string by PreferenceExt(BaseApplication.AppContext, "testStr", "")
    var long by PreferenceExt(BaseApplication.AppContext, "long", 0L)
    var float by PreferenceExt(BaseApplication.AppContext, "float", 0f)
    var boolean by PreferenceExt(BaseApplication.AppContext, "boolean", false)
    var intValue by PreferenceExt(BaseApplication.AppContext, "AAAAAAA", 0)
    var spData by PreferenceExt(BaseApplication.AppContext, "testData", null,TestData::class.java)

}