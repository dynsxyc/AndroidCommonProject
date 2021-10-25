package com.dyn.base.ui.base.recycler.addImage

import com.dyn.base.customview.BaseCustomModel

data class AddImageItemBean(
    var isAdd: Boolean = false,
    var isEditable:Boolean = true,
    var successUrl: String? = null
) : BaseCustomModel