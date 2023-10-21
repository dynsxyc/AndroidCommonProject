package com.dyn.base.ui.base.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.BR

open class BaseSingleAdapter<T : BaseCustomModel, DB : ViewDataBinding>(
    @LayoutRes val layoutId: Int,
    private val clickAction: ICustomViewActionListener? = null
) : BaseSingleItemAdapter<T, RecyclerViewHolder<DB>>() {
    override fun onBindViewHolder(holder: RecyclerViewHolder<DB>, item: T?) {
        holder.binding?.let {
            it.setVariable(BR.vm,item)
            it.setVariable(BR.action,clickAction)
            it.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder<DB> {
        val holder = RecyclerViewHolder<DB>(layoutId,parent)
        onCreateBinding(holder.binding)
        return holder
    }
    /**
     * 创建viewHolder的时候 创建绑定
     * */
    protected open fun onCreateBinding(binding: DB?){

    }
}