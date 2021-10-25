package com.dyn.base.ui.base.recycler.addImage

import com.dyn.base.R
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.databinding.ItemDefaultAddimgBindingImpl
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter

class CommonAddMoreImageAdapter(action:ICustomViewActionListener) :BaseRecyclerAdapter<AddImageItemBean, ItemDefaultAddimgBindingImpl>(R.layout.item_default_addimg,action)