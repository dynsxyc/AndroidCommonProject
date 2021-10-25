package com.dyn.base.ui.magicindicator

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

object BindingIndicatorAdapter {
    @BindingAdapter(
        value = ["magicIndicatorConfig", "magicAdapter", "magicIndicatorData", "magicBindPager"],
        requireAll = false
    )
    @JvmStatic
    fun initMagicIndicator(
        view: MagicIndicator,
        config: MagicIndicatorNavigatorConfig?,
        adapter: BaseMagicIndicatorAdapter<*>,
        data: Collection<Nothing>,
        @IdRes pagerId: Int
    ) {
        if (view.navigator == null) {
            view.navigator = CommonNavigator(view.context)
        }
        val pagerView2 = findPagerView(view, pagerId)
        pagerView2?.let {
            if (it is ViewPager2) {
                ViewPager2Helper.bind(view, it)
            }
            if (it is ViewPager) {
                ViewPagerHelper.bind(view, it)
            }
        }
        view.navigator.also {
            if (it is CommonNavigator) {
                config?.let { cf ->
                    it.isAdjustMode = cf.mAdjustMode
                    it.isEnablePivotScroll = cf.mEnablePivotScroll
                    it.isFollowTouch = cf.mFollowTouch
                    it.isIndicatorOnTop = cf.mIndicatorOnTop
                    it.isReselectWhenLayout = cf.mReselectWhenLayout
                    it.isSkimOver = cf.mSkimOver
                    it.isSmoothScroll = cf.mSmoothScroll
                    it.scrollPivotX = cf.mScrollPivotX
                    it.leftPadding = cf.mLeftPadding
                    it.rightPadding = cf.mRightPadding
                }

                if (it.adapter == null) {
                    it.adapter = adapter
                }
                it.adapter.also { adapter ->
                    if (adapter is BaseMagicIndicatorAdapter<*>) {
                        adapter.setList(data)
                        pagerView2?.let { pagerView2 ->
                            adapter.addOnPageSelectedListener(object : OnPageSelectedListener {
                                override fun onItemSelected(position: Int) {
                                    if (pagerView2 is ViewPager2) {
                                        pagerView2.currentItem = position
                                    }
                                    if (pagerView2 is ViewPager) {
                                        pagerView2.currentItem = position
                                    }
                                }
                            })
                        }

                    }
                }
            }
        }

    }
    /**
     * 根据pagerId 从parent同级向上级寻找 一直到寻找到id对应的view为止
     * */
    private fun findPagerView(parent: ViewGroup, pagerId: Int): View? {
        var result: View? = null
        var p: ViewParent? = parent
        while (result == null) {
            if (p != null && p is ViewGroup) {
                result = p.findViewById(pagerId)
                if (p.id == android.R.id.content){
                    break
                }
                p = p?.parent
            }
        }
        return result
    }
}