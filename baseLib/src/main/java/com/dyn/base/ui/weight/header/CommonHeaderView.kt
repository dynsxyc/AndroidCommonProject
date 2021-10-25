package com.dyn.base.ui.weight.header

import android.content.Context
import android.util.AttributeSet
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.databinding.LayoutTitleBindingImpl

class CommonHeaderView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : BaseCustomView<LayoutTitleBindingImpl, CommonHeaderModel>(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        id = R.id.mCommonHeaderView
    }
    override fun getViewLayoutId(): Int {
        return R.layout.layout_title
    }

    override fun getDataVariableId(): Int {
        return BR.vm
    }

    override fun getClickVariableId(): Int {
        return BR.action
    }
}