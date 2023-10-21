package com.dyn.base.binding_adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.suke.widget.SwitchButton
import com.suke.widget.SwitchButton.OnCheckedChangeListener

object BindingSwitchButtonAdapter {

    @JvmStatic
    @BindingAdapter("switchBtChange")
    fun switchBtChange(
        sBt: SwitchButton,
        newValue: Boolean
    ) {
        if (sBt.isChecked != newValue)
            sBt.isChecked = newValue
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "switchBtChange",
        event = "switchBtAttrChanged"
    )
    fun isSwitchBtChange(sBt: SwitchButton): Boolean = sBt.isChecked

    @JvmStatic
    @BindingAdapter("switchBtChangeInt")
    fun switchBtChangeInt(
        sBt: SwitchButton,
        newValue: Int
    ) {
        if (sBt.isChecked != (newValue == 1))
            sBt.isChecked = newValue == 1
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "switchBtChangeInt",
        event = "switchBtAttrChanged"
    )
    fun isSwitchBtChangeInt(sBt: SwitchButton): Int = if (sBt.isChecked) 1 else 0

    @JvmStatic
    @BindingAdapter(
        "switchBtAttrChanged",
        requireAll = false
    )
    fun setOnSwitchBtCheckedListener(
        swipeRefreshLayout: SwitchButton,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null)
            swipeRefreshLayout.setOnCheckedChangeListener { view, isChecked ->
                bindingListener.onChange()
            }
    }

    @JvmStatic
    @BindingAdapter(
        "switchListener",
        requireAll = false
    )
    fun setSwitchListener(
        swipeRefreshLayout: SwitchButton,
        bindingListener: OnCheckedChangeListener?
    ) {
        if (bindingListener != null)
            swipeRefreshLayout.setOnCheckedChangeListener(bindingListener)
    }
}