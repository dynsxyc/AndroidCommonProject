package com.dyn.base.ui.navigationbar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.dyn.base.databinding.LayoutTitleBindingImpl
import com.dyn.base.ui.weight.header.CommonHeaderBindingAdapter
import com.dyn.base.ui.weight.header.CommonHeaderView
import com.dyn.base.ui.weight.header.CommonHeaderModel

class DefaultNavigationBar(private val mBuilder:Builder) : AbsNavigationBar<DefaultNavigationBar.Builder,LayoutTitleBindingImpl,CommonHeaderModel,CommonHeaderView>(mBuilder) {
    override fun attachParent(navigationBar: View, parent: ViewGroup) {
        val lp = parent.layoutParams
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        parent.addView(navigationBar, 0,lp)
    }
    class Builder(context: Context,  parent:ViewGroup) : AbsNavigationBar.Builder<Builder, LayoutTitleBindingImpl, CommonHeaderModel,CommonHeaderView>(context, CommonHeaderView(context),parent){
        fun hOffset(hOffset:Float): Builder {
            CommonHeaderBindingAdapter.hOffset(mNavigationBar,hOffset)
            return this
        }
        override fun create(): AbsNavigationBar<Builder, LayoutTitleBindingImpl, CommonHeaderModel,CommonHeaderView> {
            return DefaultNavigationBar(this)
        }

    }
}