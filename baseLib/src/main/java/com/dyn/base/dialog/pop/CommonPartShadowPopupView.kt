package com.dyn.base.dialog.pop

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.lxj.xpopup.impl.PartShadowPopupView

class CommonPartShadowPopupView(context:Context, private val contentView:View,val params:ViewGroup.LayoutParams) : PartShadowPopupView(context) {
    override fun addInnerContent() {
        attachPopupContainer.addView(contentView,params)
    }
}