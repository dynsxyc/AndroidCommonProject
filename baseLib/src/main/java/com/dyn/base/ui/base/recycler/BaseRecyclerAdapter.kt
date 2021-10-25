package com.dyn.base.ui.base.recycler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyn.base.BR
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener

abstract class BaseRecyclerAdapter<T:BaseCustomModel, DB : ViewDataBinding>(@LayoutRes layoutId: Int,
                                                                            private val clickAction:ICustomViewActionListener? = null,
                                                                            data: MutableList<T>? = null) : BaseQuickAdapter<T, RecyclerViewHolder<DB>>(layoutId,data),Cloneable {

    constructor(@LayoutRes layoutId: Int, clickAction:ICustomViewActionListener? = null):this(layoutId,clickAction,null)

    private var mVariableData = lazy { mutableMapOf<Int,Any?>()}.value

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<DB> {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        onCreateBinding(holder.dataBinding)
        return holder
    }
    override fun convert(holder: RecyclerViewHolder<DB>, item: T) {
        holder.dataBinding?.let { binding->
            binding.setVariable(getDataVariableId(), item)
            binding.setVariable(BR.action, clickAction)
            binding.setVariable(BR.position,holder.layoutPosition)
            binding.setVariable(BR.isFirst, holder.layoutPosition == 0)
            binding.setVariable(BR.isLast, holder.layoutPosition == itemCount-1)
            mVariableData.iterator().forEach {item->
                item.value?.let {
                    binding.setVariable(item.key,it)
                }
            }
            binding.executePendingBindings()
        }
    }

    protected open fun getDataVariableId():Int{
        return BR.vm
    }
    /**
     * 创建viewHolder的时候 创建绑定
     * */
    protected open fun onCreateBinding(binding: DB?){

    }

    fun addVariableData(variableId:Int,data:Any?): BaseRecyclerAdapter<T, DB> {
        mVariableData[variableId] = data
        return this
    }
}
