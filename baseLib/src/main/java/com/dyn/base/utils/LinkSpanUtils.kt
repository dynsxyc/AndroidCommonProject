package com.dyn.base.utils

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View

interface OnOpenWebUrlListener{
    fun open(url:String)
}

object LinkSpanUtils {
    private fun setLinkClickable(clickableHtmlBuilder: SpannableStringBuilder, urlSpan: URLSpan,listener: OnOpenWebUrlListener? = null) {
        val start = clickableHtmlBuilder.getSpanStart(urlSpan)
        val end = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val originUrl = urlSpan.url;//获取url地址
                //do something
                listener?.open(originUrl)
            }

        }
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    fun getClickableHtml(spannedHtml: Spanned,listener: OnOpenWebUrlListener? = null): CharSequence {
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length, URLSpan::class.java)
        urls.forEach {
            setLinkClickable(clickableHtmlBuilder, it,listener)
        }
        return clickableHtmlBuilder
    }
}