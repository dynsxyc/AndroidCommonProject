package com.dyn.base.ui.base.pager

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.dyn.base.mvvm.model.BaseModel
import com.dyn.base.mvvm.viewmodel.BaseNetViewModel
import com.dyn.base.mvvm.viewmodel.BasePageViewModel
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.base.recycler.BasePager2Adapter
import com.dyn.base.ui.magicindicator.BaseMagicIndicatorAdapter
import com.dyn.base.ui.magicindicator.MagicIndicatorNavigatorConfig
import com.dyn.base.ui.magicindicator.BaseCommonMagicIndicatorAdapter
import com.dyn.base.ui.magicindicator.IMagicItem

abstract class BasePagePagerViewModel<PAGE_MODEL : BaseModel<*, PAGE_DATA>, PAGE_DATA> : BasePageViewModel<PAGE_MODEL,PAGE_DATA>() {
    open val indicatorBg = MutableLiveData(com.blankj.utilcode.util.ColorUtils.getColor(com.dyn.base.R.color.white))
    open val indicatorHeight = MutableLiveData(dp2px(50f))
    val mMagicData = MutableLiveData(createMagicData())
    val mMagicConfig = createMagicConfig()
    val magicAdapter = MutableLiveData(createMagicAdapter())
    val pagerPosition = MutableLiveData(0)

    abstract fun createMagicData(): MutableList<out IMagicItem>

    protected open fun createMagicAdapter(): BaseMagicIndicatorAdapter<out IMagicItem> {
        return BaseCommonMagicIndicatorAdapter()
    }

    protected open fun createMagicConfig(): MagicIndicatorNavigatorConfig {
        return MagicIndicatorNavigatorConfig(
            mAdjustMode = true
        )
    }

}