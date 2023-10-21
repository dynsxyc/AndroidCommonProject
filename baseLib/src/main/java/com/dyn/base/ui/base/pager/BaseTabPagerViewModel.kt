package com.dyn.base.ui.base.pager

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.dyn.base.mvvm.model.BaseModel
import com.dyn.base.mvvm.viewmodel.BaseNetViewModel
import com.dyn.base.ui.magicindicator.BaseMagicIndicatorAdapter
import com.dyn.base.ui.magicindicator.MagicIndicatorNavigatorConfig
import com.dyn.base.ui.magicindicator.BaseCommonMagicIndicatorAdapter
import com.dyn.base.ui.magicindicator.IMagicItem

abstract class BaseTabPagerViewModel : BaseNetViewModel() {
    val mMagicData = MutableLiveData(createMagicData())
    val pagerPosition = MutableLiveData(0)
    abstract fun createMagicData(): MutableList<out IMagicItem>
    override fun createDataModels(): MutableList<BaseModel<*, Any?>>? {
        return null
    }
}