package com.dyn.base.utils.switchover

import androidx.databinding.ObservableField
import com.dyn.base.customview.BaseCustomModel

data class HostBean(val isChecked: ObservableField<Boolean> = ObservableField<Boolean>(), val hostName: String, val hostUrl:String,val hostWebUrl:String):BaseCustomModel

data class SwitchoverHostModel(
    val currentHost: ObservableField<String> = ObservableField<String>(),
    val btStr: ObservableField<String> = ObservableField<String>(),
    val editHost: ObservableField<String> = ObservableField<String>(),
    val hosts:ObservableField<MutableList<HostBean>> = ObservableField(),
    val isSandBoxPay:ObservableField<Boolean> = ObservableField(false)
):BaseCustomModel
