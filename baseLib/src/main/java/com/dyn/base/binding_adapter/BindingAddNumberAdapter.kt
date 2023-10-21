package com.dyn.base.binding_adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.Observable
import com.dyn.base.ui.addnumber.AddNumberView
import com.dyn.base.ui.addnumber.IOnNumberChangeListener

object BindingAddNumberAdapter {

    @JvmStatic
    @BindingAdapter("numberValue")
    fun changeNumber(
        view: AddNumberView,
        newValue: Int
    ) {
        if (newValue != view.mViewModel?.count?.get())
            view.mViewModel?.count?.set(newValue)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "numberValue",
        event = "numberChanged"
    )
    fun getNumberValue(view: AddNumberView): Int = view.mViewModel?.count?.get()!!

    @JvmStatic
    @BindingAdapter(
        "numberChanged"
    )
    fun setOnSwitchBtCheckedListener(
        addNumberView: AddNumberView,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null)
            addNumberView.setOnPropertyChangedCallback(object : IOnNumberChangeListener {
                override fun onChange() {
                    bindingListener.onChange()
                }
            })
    }

    @JvmStatic
    @BindingAdapter("numberMaxValue")
    fun numberMaxValue(
        view: AddNumberView,
        newValue: Int
    ) {
        view.mViewModel?.maxCount?.set(newValue)
    }

    @JvmStatic
    @BindingAdapter("numberMinValue")
    fun numberMinValue(
        view: AddNumberView,
        newValue: Int
    ) {
        view.mViewModel?.minCount?.set(newValue)
    }
}