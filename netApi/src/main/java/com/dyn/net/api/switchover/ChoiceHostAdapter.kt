package com.dyn.net.api.switchover

import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter
import com.dyn.net.api.R
import com.dyn.net.api.databinding.ItemChoiceHostBinding

class ChoiceHostAdapter(action:ICustomViewActionListener):BaseRecyclerAdapter<HostBean,ItemChoiceHostBinding>(R.layout.item_choice_host,action) {
}