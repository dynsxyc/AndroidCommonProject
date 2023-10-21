package com.dyn.base.ui.flowlayout

import android.view.Gravity
import androidx.databinding.ObservableField
import com.dyn.base.customview.BaseCustomModel

abstract class ISingleContent:BaseCustomModel {
    abstract fun getShowText():String?
    private var checked: ObservableField<Boolean>? = null
    open fun isChecked(): ObservableField<Boolean> {
        if (checked == null) {
            checked = ObservableField(false)
        }
        return checked!!
    }
    private var enable: ObservableField<Boolean>? = null
    fun isEnable(): ObservableField<Boolean> {
        if (enable == null) {
            enable = ObservableField(true)
        }
        return enable!!
    }
    fun getTextGravity(position:Int):Int{
        return when (position%3){
            1->Gravity.CENTER
            2->Gravity.END
            else->Gravity.START
        }
    }
}