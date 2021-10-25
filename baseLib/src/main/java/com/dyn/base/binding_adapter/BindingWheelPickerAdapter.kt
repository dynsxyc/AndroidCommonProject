package com.dyn.base.binding_adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.aigestudio.wheelpicker.WheelPicker
import com.dyn.base.ui.flowlayout.IStringContent

object BindingWheelPickerAdapter {
    /**
     * wheelPicker  position变动监听  start
     * */
    @JvmStatic
    @BindingAdapter("changePickerPosition")
    fun switchBtChange(
        sBt: WheelPicker,
        newValue: Int
    ) {
        if (sBt.currentItemPosition != newValue)
            sBt.setSelectedItemPosition(newValue, true)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "changePickerPosition",
        event = "changePickerPositionEvent"
    )
    fun isSwitchBtChange(sBt: WheelPicker): Int = sBt.currentItemPosition

    /**
     * wheelPicker  position变动监听  end
     * */

    @JvmStatic
    @BindingAdapter("changePickerPositionData")
    fun changePickerPositionData(
        sBt: WheelPicker,
        newValue: Any?
    ) {
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "changePickerPositionData",
        event = "changePickerPositionEvent"
    )
    fun changePickerPositionData(sBt: WheelPicker): Any? = sBt.data[sBt.currentItemPosition]

    @JvmStatic
    @BindingAdapter(
        "changePickerPositionEvent",
        requireAll = false
    )
    fun setOnSwitchBtCheckedListener(
        swipeRefreshLayout: WheelPicker,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null)
            swipeRefreshLayout.setOnItemSelectedListener { picker, data, position ->
                bindingListener.onChange()
            }
    }

    @JvmStatic
    @BindingAdapter("pickerBindData")
    fun pickerBindData(
        picker: WheelPicker,
        mutableList: Collection<Any>?
    ) {
        mutableList?.let { data ->
            val set = data.map {
                if (it is IStringContent)
                    it.content
                else
                    ""
            }
            picker.data = set
        }
    }
}