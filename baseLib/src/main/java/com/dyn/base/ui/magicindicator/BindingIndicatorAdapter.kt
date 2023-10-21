package com.dyn.base.ui.magicindicator

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ScrollState
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

object BindingIndicatorAdapter {
    @BindingAdapter(
        value = ["magicIndicatorConfig", "magicAdapter", "magicIndicatorData", "magicBindPager", "magicChangeListener"],
        requireAll = false
    )
    @JvmStatic
    fun initMagicIndicator(
        view: MagicIndicator,
        config: MagicIndicatorNavigatorConfig?,
        adapter: BaseMagicIndicatorAdapter<*>?,
        data: Collection<Nothing>?,
        @IdRes pagerId: Int,
        listener: OnPageSelectedListener?
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

                if (it.adapter == null && adapter != null) {
                    it.adapter = adapter
                    adapter.addOnPageSelectedListener(object : OnPageSelectedListener {
                        override fun onItemSelected(position: Int, itemData: Any?) {
                            if (pagerView2 is ViewPager2) {
                                pagerView2.currentItem = position
                            }
                            if (pagerView2 is ViewPager) {
                                pagerView2.currentItem = position
                            }
                            listener?.let {
                                view.onPageSelected(position)
                                it.onItemSelected(position, itemData)
                            }
                        }
                    })

                }
                adapter?.setList(data)
            }
        }

    }


    @BindingAdapter(value = ["magicSetPosition"])
    @JvmStatic
    fun magicSetPosition(
        view: MagicIndicator, position: Int
    ) {
        view.onPageSelected(position)
    }

    @BindingAdapter(value = ["bindNavigator"])
    @JvmStatic
    fun bindNavigator(
        view: MagicIndicator, navigator: IPagerNavigator?
    ) {
        if (navigator != null) {
            view.navigator = navigator
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
                if (p.id == android.R.id.content) {
                    break
                }
                p = p?.parent
            }
        }
        return result
    }
}