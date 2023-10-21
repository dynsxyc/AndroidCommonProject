package com.dyn.base.ui.weight

import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import com.dyn.base.binding_adapter.BindingCommonAdapter

interface IClickableText{
    var showText:String
    var textColor:Int
}

class CustomClickableSpan<T:IClickableText>(
    val content: T,
    private val searchListener: BindingCommonAdapter.IActionListener<T>?
) : URLSpan(content.showText) {
    override fun updateDrawState(ds: TextPaint) {
        ds.color = content.textColor
        ds.isUnderlineText = false
    }

    override fun onClick(widget: View) {
        searchListener?.onSearch(content)
    }
}