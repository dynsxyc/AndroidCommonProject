package com.dyn.base.ui.base.recycler

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyn.base.BR
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.orhanobut.logger.Logger

abstract class BaseRecyclerAdapter<T:BaseCustomModel, DB : ViewDataBinding>(@LayoutRes val layoutId: Int,
                                                                            private val clickAction:ICustomViewActionListener? = null,open var item: List<T> = emptyList()) : BaseQuickAdapter<T, RecyclerViewHolder<DB>>(item),Cloneable {


    private var mVariableData = lazy { mutableMapOf<Int,Any?>()}.value

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder<DB> {

        val holder = RecyclerViewHolder<DB>(layoutId,parent)
        onCreateBinding(holder.binding)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<DB>, position: Int, item: T?) {
        holder.binding?.let { binding->
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

    override fun getItemCount(items: List<T>): Int {
        val count = try {
            super.getItemCount(items)
        }catch (e:Exception){
            Logger.i("getItemCount is exception ${this.javaClass.simpleName}")
            0
        }
        return count
    }
}
