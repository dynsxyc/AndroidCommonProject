package com.dyn.base.ui.weight.header

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.blankj.utilcode.util.ConvertUtils.sp2px

object CommonHeaderBindingAdapter {
    @BindingAdapter(value = ["finishDrawableStart", "finishDrawableTop", "finishDrawableEnd", "finishDrawableBottom", "finishText", "finishTextColor", "finishTextSize", "finishMessageCount", "finishIsShowMessageStatus"], requireAll = false)
    @JvmStatic
    fun hFinishStyle(headerView: CommonHeaderView, finishDrawableStart: Drawable?, finishDrawableTop: Drawable?, finishDrawableEnd: Drawable?, finishDrawableBottom: Drawable?,
                     finishText: String?, finishTextColor: Int, finishTextSize: Int, finishMessageCount: Int, finishIsShowMessageStatus: Boolean) {
        val style = headerView.mViewModel?.finishStyle
        finishDrawableStart?.let {
            style?.drawableStart?.set(finishDrawableStart)
        }
        finishDrawableTop?.let {
            style?.drawableTop?.set(finishDrawableTop)
        }
        finishDrawableEnd?.let {
            style?.drawableEnd?.set(finishDrawableEnd)
        }
        finishDrawableBottom?.let {
            style?.drawableBottom?.set(finishDrawableBottom)
        }
        finishText?.let {
            style?.text?.set(finishText)
        }
        if (finishTextColor > 0) {
            style?.textColor?.set(finishTextColor)
        }
        if (finishTextSize > 0) {
            style?.textSize?.set(sp2px(finishTextSize.toFloat()))
        }
        style?.messageCount?.set(finishMessageCount)
        style?.isMessageStatus?.set(finishIsShowMessageStatus)
    }

    @BindingAdapter(value = ["backDrawableStart", "backDrawableTop", "backDrawableEnd", "backDrawableBottom", "backText", "backTextColor", "backTextSize", "backMessageCount", "backIsShowMessageStatus"], requireAll = false)
    @JvmStatic
    fun hBackStyle(headerView: CommonHeaderView, backDrawableStart: Drawable?, backDrawableTop: Drawable?, backDrawableEnd: Drawable?, backDrawableBottom: Drawable?,
                     backText: String?, backTextColor: Int, backTextSize: Int, backMessageCount: Int, backIsShowMessageStatus: Boolean) {
        val style = headerView.mViewModel?.backStyle
        backDrawableStart?.let {
            style?.drawableStart?.set(backDrawableStart)
        }
        backDrawableTop?.let {
            style?.drawableTop?.set(backDrawableTop)
        }
        backDrawableEnd?.let {
            style?.drawableEnd?.set(backDrawableEnd)
        }
        backDrawableBottom?.let {
            style?.drawableBottom?.set(backDrawableBottom)
        }
        backText?.let {
            style?.text?.set(backText)
        }
        if (backTextColor > 0) {
            style?.textColor?.set(backTextColor)
        }
        if (backTextSize > 0) {
            style?.textSize?.set(sp2px(backTextSize.toFloat()))
        }
        style?.messageCount?.set(backMessageCount)
        style?.isMessageStatus?.set(backIsShowMessageStatus)
    }

    @BindingAdapter(value = ["rightDrawableStart", "rightDrawableTop", "rightDrawableEnd", "rightDrawableBottom", "rightText", "rightTextColor", "rightTextSize", "rightMessageCount", "rightIsShowMessageStatus"], requireAll = false)
    @JvmStatic
    fun hRightStyle(headerView: CommonHeaderView, rightDrawableStart: Drawable?, rightDrawableTop: Drawable?, rightDrawableEnd: Drawable?, rightDrawableBottom: Drawable?,
                     rightText: String?, rightTextColor: Int, rightTextSize: Int, rightMessageCount: Int, rightIsShowMessageStatus: Boolean) {
        val style = headerView.mViewModel?.rightStyle
        rightDrawableStart?.let {
            style?.drawableStart?.set(rightDrawableStart)
        }
        rightDrawableTop?.let {
            style?.drawableTop?.set(rightDrawableTop)
        }
        rightDrawableEnd?.let {
            style?.drawableEnd?.set(rightDrawableEnd)
        }
        rightDrawableBottom?.let {
            style?.drawableBottom?.set(rightDrawableBottom)
        }
        rightText?.let {
            style?.text?.set(rightText)
        }
        if (rightTextColor > 0) {
            style?.textColor?.set(rightTextColor)
        }
        if (rightTextSize > 0) {
            style?.textSize?.set(sp2px(rightTextSize.toFloat()))
        }
        style?.messageCount?.set(rightMessageCount)
        style?.isMessageStatus?.set(rightIsShowMessageStatus)
    }

    @BindingAdapter(value = ["rightLastDrawableStart", "rightLastDrawableTop", "rightLastDrawableEnd", "rightLastDrawableBottom", "rightLastText", "rightLastTextColor", "rightLastTextSize", "rightLastMessageCount", "rightLastIsShowMessageStatus"], requireAll = false)
    @JvmStatic
    fun hRightLastStyle(headerView: CommonHeaderView, rightLastDrawableStart: Drawable?, rightLastDrawableTop: Drawable?, rightLastDrawableEnd: Drawable?, rightLastDrawableBottom: Drawable?,
                     rightLastText: String?, rightLastTextColor: Int, rightLastTextSize: Int, rightLastMessageCount: Int, rightLastIsShowMessageStatus: Boolean) {
        val style = headerView.mViewModel?.rightLastStyle
        rightLastDrawableStart?.let {
            style?.drawableStart?.set(rightLastDrawableStart)
        }
        rightLastDrawableTop?.let {
            style?.drawableTop?.set(rightLastDrawableTop)
        }
        rightLastDrawableEnd?.let {
            style?.drawableEnd?.set(rightLastDrawableEnd)
        }
        rightLastDrawableBottom?.let {
            style?.drawableBottom?.set(rightLastDrawableBottom)
        }
        rightLastText?.let {
            style?.text?.set(rightLastText)
        }
        if (rightLastTextColor > 0) {
            style?.textColor?.set(rightLastTextColor)
        }
        if (rightLastTextSize > 0) {
            style?.textSize?.set(sp2px(rightLastTextSize.toFloat()))
        }
        style?.messageCount?.set(rightLastMessageCount)
        style?.isMessageStatus?.set(rightLastIsShowMessageStatus)
    }
    @BindingAdapter(value = ["hOffset"])
    @JvmStatic
    fun hOffset(headerView: CommonHeaderView, scrollOffset: Float){
        val viewModel = headerView.mViewModel
        val color = viewModel?.bg?.get()
        color?.let {
           val rColor =  Color.argb((255*scrollOffset).toInt(),Color.red(color),Color.green(color),Color.blue(color))
            viewModel.bg.set(rColor)
        }
        viewModel?.titleAlpha?.set(scrollOffset)
    }


}