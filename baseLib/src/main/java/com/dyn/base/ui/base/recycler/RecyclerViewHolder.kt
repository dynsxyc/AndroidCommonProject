package com.dyn.base.ui.base.recycler

import android.view.View
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

class RecyclerViewHolder<BD:ViewDataBinding> constructor(view:View): BaseDataBindingHolder<BD>(view) {
}