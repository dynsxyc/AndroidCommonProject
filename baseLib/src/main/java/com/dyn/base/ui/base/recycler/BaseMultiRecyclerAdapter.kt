package com.dyn.base.ui.base.recycler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.dyn.base.BR
import com.dyn.base.customview.BaseMultiItemCustomModel
import com.dyn.base.customview.ICustomViewActionListener

abstract class BaseMultiRecyclerAdapter<T : BaseMultiItemCustomModel>(val itemAction:ICustomViewActionListener? = null) : BaseMultiItemQuickAdapter<T, RecyclerViewHolder<*>>() {
    private var mVariableData = lazy { mutableMapOf<Int, Any>() }.value

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<out ViewDataBinding> {
        val holder = super.onCreateViewHolder(parent, viewType)
        onCreateBinding(holder.dataBinding,viewType)
        return holder
    }
    override fun convert(holder: RecyclerViewHolder<*>, item: T) {
        holder.dataBinding?.let { binding ->
            binding.setVariable(getDataVariableId(), item)
            binding.setVariable(BR.action,itemAction)
            mVariableData.iterator().forEach {
                binding.setVariable(it.key, it.value)
            }
            binding.executePendingBindings()
        }
    }

    protected open fun getDataVariableId():Int{
        return BR.vm
    }

    fun addVariableData(variableId: Int, data: Any): BaseMultiRecyclerAdapter<T> {
        mVariableData[variableId] = data
        return this
    }
    /**
     * 创建viewHolder的时候 创建绑定
     * */
    protected open fun onCreateBinding(binding: ViewDataBinding?,viewType: Int){

    }
    /**
     * 调用此方法，设置多布局
     * @param type Int
     * @param layoutResId Int
     */
    fun addType(type: Int, @LayoutRes layoutResId: Int) {
        super.addItemType(type, layoutResId)
    }

}