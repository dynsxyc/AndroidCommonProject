package com.dyn.base.ui.magicindicator

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel
import com.jakewharton.rxbinding4.view.clicks
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.concurrent.TimeUnit

abstract class IMagicItem: BaseCustomModel {
    abstract fun getShowTabName(): String
    abstract fun getPagerId(): Long
    open fun getWeightOff(): Float = 1.0f
    open fun getShowTabIconRes(): Int = 0
    abstract fun getTabType(): Int

    private var checked: ObservableField<Boolean>? = null

    fun isChecked(): ObservableField<Boolean> {
        if (checked == null) {
            checked = ObservableField(false)
        }
        return checked!!
    }
}
open class BaseCommonMagicIndicatorAdapter : BaseMagicIndicatorAdapter<IMagicItem>() {
    @ColorInt val selectedColor: Int = ColorUtils.getColor(R.color.colorPrimary)
    @ColorInt val normalColor: Int = Color.parseColor("#0D0D0D")
    private val selectedTestSize: Float = 14f
    private val normalTestSize: Float = 14f
    private val indicatorType: Int = 0
    private val paddingH: Int = dp2px(10f)
    @ColorInt private val pagerIndicatorColor :Int = ColorUtils.getColor(R.color.colorPrimary)
    val indicatorLineHeight: Float = dp2px(3f).toFloat()
    val indicatorLineYOffset: Float = dp2px(8f).toFloat()
    override fun createTitleView(context: Context, index: Int): IPagerTitleView {
        val itemData = getItem(index)
        val simplePagerTitleView :SimplePagerTitleView = (object :SimplePagerTitleView(context){
            override fun onSelected(index: Int, totalCount: Int) {
                super.onSelected(index, totalCount)
                textSize = selectedTestSize
            }

            override fun onDeselected(index: Int, totalCount: Int) {
                super.onDeselected(index, totalCount)
                textSize = normalTestSize
            }
        })
        simplePagerTitleView.let {
            it.setPadding(paddingH, 0, paddingH, 0)
            it.text = itemData?.getShowTabName()
            it.selectedColor = selectedColor
            it.normalColor = normalColor
            it.clicks().throttleFirst(300, TimeUnit.MILLISECONDS).subscribe {
                try {
                    onPageSelected(index,itemData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
       return simplePagerTitleView
    }

    override fun createPagerIndicator(context: Context): IPagerIndicator {
       return LinePagerIndicator(context).also {
            it.setColors(pagerIndicatorColor)
            it.mode = LinePagerIndicator.MODE_WRAP_CONTENT
            it.lineHeight= indicatorLineHeight
            it.roundRadius = indicatorLineHeight/2
            it.yOffset = indicatorLineYOffset
        }
    }

}