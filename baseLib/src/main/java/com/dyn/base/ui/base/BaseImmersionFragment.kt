package com.dyn.base.ui.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.Nullable
import com.dyn.base.ui.databinding.DataBindingFragment
import com.gyf.immersionbar.components.ImmersionOwner
import com.gyf.immersionbar.components.ImmersionProxy
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 这个类主要实现immersionBar 功能
 * */
abstract class BaseImmersionFragment : DataBindingFragment(), ImmersionOwner {
    override fun onCreate(savedInstanceState: Bundle?) {
        onLazyBeforeView()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        if (isInPager()){
            val parentFragment = parentFragment
            userVisibleHint = parentFragment?.userVisibleHint?:true
        }else
        if (userVisibleHint) {
            onVisible()
        }
        super.onResume()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            onVisible()
        } else {
            onInvisible()
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
    }

    override fun onPause() {
        if (isInPager()) {
            userVisibleHint = false
        }else{
            onInvisible()
        }
        super.onPause()
    }

    protected open fun isInPager(): Boolean {
        return false
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * onCreated中调用的
     * On lazy after view.
     */
    override fun onLazyBeforeView() {}

    /**
     * 懒加载，在view初始化完成之后执行
     * onActivityCreated 中调用的
     * On lazy before view.
     */
    override fun onLazyAfterView() {}

    override fun initImmersionBar() {
        immersionBar {
            autoDarkModeEnable(true, 0.5f)
            statusBarDarkFont(true)
            keyboardEnable(
                true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            )
            transparentStatusBar()
        }
    }

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    override fun onVisible() {
        if (immersionBarEnabled()) {
            initImmersionBar()
        }
    }

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    override fun onInvisible() {}

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return false
    }
}