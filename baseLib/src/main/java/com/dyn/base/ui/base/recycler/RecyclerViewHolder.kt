package com.dyn.base.ui.base.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.DataBindingHolder

class RecyclerViewHolder<BD:ViewDataBinding> (view: View): DataBindingHolder<BD>(view) {
        constructor(@LayoutRes resId:Int,parent:ViewGroup):this(LayoutInflater.from(parent.context).inflate(resId, parent, false))
}