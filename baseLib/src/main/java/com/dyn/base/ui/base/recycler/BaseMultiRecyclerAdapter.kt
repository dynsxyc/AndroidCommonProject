package com.dyn.base.ui.base.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.dyn.base.BR
import com.dyn.base.customview.BaseMultiItemCustomModel
import com.dyn.base.customview.ICustomViewActionListener

abstract class BaseMultiRecyclerAdapter<T : BaseMultiItemCustomModel>(val itemAction: ICustomViewActionListener? = null) :
    BaseMultiItemAdapter<T>() {
    private var mVariableData = lazy { mutableMapOf<Int, Any>() }.value

    protected open fun getDataVariableId(): Int {
        return BR.vm
    }

    fun addVariableData(variableId: Int, data: Any): BaseMultiRecyclerAdapter<T> {
        mVariableData[variableId] = data
        return this
    }

    /**
     * 创建viewHolder的时候 创建绑定
     * */
    protected open fun onCreateBinding(binding: ViewDataBinding?, viewType: Int) {

    }

    /**
     * 调用此方法，设置多布局
     * @param type Int
     * @param layoutResId Int
     */
    open fun <DB : ViewDataBinding> addType(type: Int, @LayoutRes layoutResId: Int) {
        super.addItemType(type, object : OnMultiItemAdapterListener<T, RecyclerViewHolder<DB>> {
            override fun onBind(holder: RecyclerViewHolder<DB>, position: Int, item: T?) {
                holder.binding?.let { binding ->
                    binding.setVariable(getDataVariableId(), item)
                    binding.setVariable(BR.action, itemAction)
                    mVariableData.iterator().forEach {
                        binding.setVariable(it.key, it.value)
                    }
                    binding.executePendingBindings()
                }
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): RecyclerViewHolder<DB> {
                val holder = RecyclerViewHolder<DB>(layoutResId, parent)
                onCreateBinding(holder.binding, viewType)
                return holder
            }

        })
    }


}