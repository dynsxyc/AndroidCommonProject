package com.dyn.base.loadsir


import android.content.Context
import android.view.View
import com.dyn.base.R

import com.kingja.loadsir.callback.Callback

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

class LoadingCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    /**
     * @method
     * @description 空页面事件响应监听
     * @date: 2019/11/29 9:18
     * @author: dyn
     * @param
     * @return true 屏蔽单击响应reload事件 false 单击调用reload事件 默认false
     */
    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}
