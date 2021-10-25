package com.dyn.net.api.switchover

import android.content.Context
import android.util.AttributeSet
import com.dyn.base.customview.BaseCustomView
import com.dyn.net.api.BR
import com.dyn.net.api.R
import com.dyn.net.api.databinding.ViewSwitchoverHostBinding

class SwitchoverHostView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : BaseCustomView<ViewSwitchoverHostBinding,SwitchoverHostModel>(context, attrs, defStyleAttr){
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun getViewLayoutId(): Int {
        return R.layout.view_switchover_host
    }

    override fun getDataVariableId(): Int {
        return BR.vm
    }

    override fun getClickVariableId(): Int {
        return BR.action
    }

    override fun getVariableData(): MutableMap<Int, Any>? {
        return mutableMapOf(
            BR.adapter to ChoiceHostAdapter(this)
        )
    }
}