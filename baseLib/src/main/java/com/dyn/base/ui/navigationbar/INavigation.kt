package com.dyn.base.ui.navigationbar

import android.view.View
import android.view.ViewGroup

interface INavigation {
    fun createNavigationBar()
    /**
     * 添加到父布局
     * */
    fun attachParent(navigationBar: View, parent: ViewGroup)
    /**
     * 绑定参数
     * */
    fun attachNavigationParams()
}