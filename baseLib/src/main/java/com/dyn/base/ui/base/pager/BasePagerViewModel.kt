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

abstract class BasePagerViewModel : BaseNetViewModel() {
    open val indicatorBg = MutableLiveData(ColorUtils.getColor(com.dyn.base.R.color.white))
    open val indicatorHeight = MutableLiveData(dp2px(50f))
    val magicIndicatorIsShow = MutableLiveData(true)
    val mMagicData = MutableLiveData(createMagicData())
    val mMagicConfig = createMagicConfig()
    val magicAdapter = MutableLiveData(createMagicAdapter())
    val pagerPosition = MutableLiveData(0)
//    val pager2Adapter = MutableLiveData<BasePager2Adapter<*>>()

    abstract fun createMagicData(): MutableList<out IMagicItem>

    protected open fun createMagicAdapter(): BaseMagicIndicatorAdapter<out IMagicItem> {
        return BaseCommonMagicIndicatorAdapter()
    }

    protected open fun createMagicConfig(): MagicIndicatorNavigatorConfig {
        return MagicIndicatorNavigatorConfig(
            mAdjustMode = true
        )
    }

    override fun createDataModels(): MutableList<BaseModel<*, Any?>>? {
        return null
    }
}