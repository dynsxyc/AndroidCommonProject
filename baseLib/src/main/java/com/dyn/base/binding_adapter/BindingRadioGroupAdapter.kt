package com.dyn.base.binding_adapter

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object BindingRadioGroupAdapter {


    @JvmStatic
    @BindingAdapter(
        value = ["checkedRadioButtonId"]
    )
    fun setCheckedRadioButtonId(group: RadioGroup, id:Int){
        group.check(id)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "checkedRadioButtonId",
        event = "radioButtonCheckedListener"
    )
    fun getCheckedRadioButtonId(group: RadioGroup): Int = group.checkedRadioButtonId

    @BindingAdapter(value = ["radioButtonCheckedListener"],requireAll = false)
    @JvmStatic
    fun setRadioButtonCheckedListener(group:RadioGroup,listener:InverseBindingListener?){
        listener?.let {
            group.setOnCheckedChangeListener { _, _ ->
                it.onChange()
            }
        }
    }
}