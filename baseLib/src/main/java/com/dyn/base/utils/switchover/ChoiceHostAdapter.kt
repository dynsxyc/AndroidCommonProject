package com.dyn.base.utils.switchover

import com.dyn.base.R
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.databinding.ItemChoiceHostBinding
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter

class ChoiceHostAdapter(action:ICustomViewActionListener):BaseRecyclerAdapter<HostBean, ItemChoiceHostBinding>(
    R.layout.item_choice_host,action) {
}