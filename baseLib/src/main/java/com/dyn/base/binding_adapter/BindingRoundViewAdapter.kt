package com.dyn.base.binding_adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.dyn.base.ui.weight.RoundEditTextView
import com.flyco.roundview.RoundLinearLayout
import com.flyco.roundview.RoundTextView
import kotlin.math.roundToInt

object BindingRoundViewAdapter {


    @BindingAdapter(value = ["rv_strokeColor"], requireAll = false)
    @JvmStatic
    fun rv_strokeColor(view: RoundTextView, color: Int) {
        view.delegate.strokeColor = color
    }

    @BindingAdapter(value = ["rv_backgroundColor"], requireAll = false)
    @JvmStatic
    fun rv_backgroundColor(view: View, color: Int) {
        when (view) {
            is RoundEditTextView ->
                view.getDelegate().backgroundColor = color
            is RoundTextView ->
                view.delegate.backgroundColor = color
            is RoundLinearLayout ->
                view.delegate.backgroundColor = color
        }
    }

    @BindingAdapter(value = ["rvStrokeWidth"], requireAll = false)
    @JvmStatic
    fun rvStrokeWidth(view: View, width: Float) {
        when (view) {
            is RoundEditTextView ->
                view.getDelegate().strokeWidth =  width.toInt()
            is RoundTextView ->
                view.delegate.strokeWidth = width.toInt()
            is RoundLinearLayout ->
                view.delegate.strokeWidth = width.toInt()
        }
    }

    @BindingAdapter(value = ["textColor"], requireAll = false)
    @JvmStatic
    fun textColor(view: RoundTextView, color: Int) {
        view.setTextColor(color)
    }

}