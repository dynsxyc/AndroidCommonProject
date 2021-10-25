package com.dyn.base.ui.base.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.BR
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder
/**
 * 多类型列表样式适配
 * */
class BaseQuickDataBindingItemBinder<T,DB:ViewDataBinding>(@LayoutRes private val layoutId:Int, private val dataVariableId:Int = BR.vm) : QuickDataBindingItemBinder<T,DB>() {
    private var mVariableData = lazy { mutableMapOf<Int, Any>() }.value
    override fun convert(holder: QuickDataBindingItemBinder.BinderDataBindingHolder<DB>, data: T) {
        holder.dataBinding?.let { binding ->
            binding.setVariable(dataVariableId, data)
            binding.setVariable(com.dyn.base.BR.adapter, this)
            mVariableData.iterator().forEach {
                binding.setVariable(it.key, it.value)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): DB {
        return DataBindingUtil.inflate(layoutInflater,layoutId,parent,false)
    }
    fun addVariableData(variableId: Int, data: Any): BaseQuickDataBindingItemBinder<T,DB> {
        mVariableData[variableId] = data
        return this
    }

}