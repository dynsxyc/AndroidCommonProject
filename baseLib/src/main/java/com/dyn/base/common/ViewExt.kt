package com.dyn.base.common

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.databinding.LayoutEmptyBindingBinding

fun LayoutInflater.queryBindingEmptyView(vm:BaseCustomModel,action:ICustomViewActionListener):View{
    val binding = DataBindingUtil.inflate<LayoutEmptyBindingBinding>(this, R.layout.layout_empty_binding,null,false)
    binding.setVariable(BR.vm,vm)
    binding.setVariable(BR.action,action)
    binding.executePendingBindings()
    return binding.root
}
