package com.dyn.base.ui.layoutmanager

import android.view.View

/**
 * 监听停止时，显示在中间的View的监听
 */
interface OnSnapSelectedViewListener {
    fun onSelectedView(view: View?, position: Int)
}