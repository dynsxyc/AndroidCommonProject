package com.dyn.base.customview

import android.view.View

interface ICustomViewActionListener {
    companion object {
        const val ACTION_ROOT_VIEW_CLICKED: String = "action_root_view_clicked"
    }

    fun onAction(view:View,action: String, viewModel: BaseCustomModel)
}
