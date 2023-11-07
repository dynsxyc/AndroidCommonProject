package com.dyn.base.binding_adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.dyn.base.ui.weight.RoundClearEditTextView
import com.dyn.base.ui.weight.RoundConstraintLayout
import com.flyco.roundview.RoundFrameLayout
import com.flyco.roundview.RoundLinearLayout
import com.flyco.roundview.RoundTextView

object BindingRoundViewAdapter {


    @BindingAdapter(value = ["rv_strokeColor"], requireAll = false)
    @JvmStatic
    fun rv_strokeColor(view: RoundTextView, color: Int) {
        view.delegate.strokeColor = color
    }


    @BindingAdapter(value = ["rvBackgroundColor"], requireAll = false)
    @JvmStatic
    fun rvBackgroundColor(view: View, color: Int) {
        val delegate = when (view) {
            is RoundClearEditTextView ->
                view.getDelegate()

            is RoundTextView ->
                view.delegate

            is RoundLinearLayout ->
                view.delegate

            is RoundConstraintLayout ->
                view.getDelegate()

            is RoundFrameLayout ->
                view.delegate

            else -> null
        }
        delegate?.let {
            it.backgroundColor = color
        }
    }

    @BindingAdapter(value = ["rv_backgroundColor"], requireAll = false)
    @JvmStatic
    fun rv_backgroundColor(view: View, color: Int) {
        when (view) {
            is RoundClearEditTextView ->
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
            is RoundClearEditTextView ->
                view.getDelegate().strokeWidth =  width.toInt()
            is RoundTextView ->
                view.delegate.strokeWidth = width.toInt()
            is RoundLinearLayout ->
                view.delegate.strokeWidth = width.toInt()
        }
    }
    @BindingAdapter(value = ["rvStrokeColor"], requireAll = false)
    @JvmStatic
    fun rvStrokeColor(view: View, color: Int) {
        when (view) {
            is RoundClearEditTextView ->
                view.getDelegate().strokeColor =  color
            is RoundTextView ->
                view.delegate.strokeColor = color
            is RoundLinearLayout ->
                view.delegate.strokeColor = color
            is RoundConstraintLayout ->
                view.getDelegate().strokeColor = color
            is RoundFrameLayout ->
                view.delegate.strokeColor = color
        }
    }

    @BindingAdapter(value = ["textColor"], requireAll = false)
    @JvmStatic
    fun textColor(view: RoundTextView, color: Int) {
        view.setTextColor(color)
    }

}