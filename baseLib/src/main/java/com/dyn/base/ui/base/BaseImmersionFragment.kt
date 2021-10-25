package com.dyn.base.ui.base

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.Nullable
import com.dyn.base.R
import com.dyn.base.ui.databinding.DataBindingFragment
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.components.ImmersionOwner
import com.gyf.immersionbar.components.ImmersionProxy
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 这个类主要实现immersionBar 功能
 * */
abstract class BaseImmersionFragment :DataBindingFragment(), ImmersionOwner{
    /**
     * ImmersionBar代理类
     */
    private val mImmersionProxy = ImmersionProxy(this)

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        mImmersionProxy.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        mImmersionProxy.onActivityCreated(savedInstanceState)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        mImmersionProxy.onResume()
        super.onResume()
    }

    override fun onPause() {
        mImmersionProxy.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mImmersionProxy.onDestroy()
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        mImmersionProxy.onHiddenChanged(hidden)
        super.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
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
            fullScreen(false)
            autoDarkModeEnable(true,0.5f)
            statusBarColor(R.color.transparent,0.6f)
        }
    }

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    override fun onVisible() {
        if (immersionBarEnabled()){
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