package com.dyn.base.ui.weight.datepicker

import android.content.Context
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.databinding.DialogChoiceDateBinding
import com.google.android.material.datepicker.CalendarConstraints

data class CustomDatePickerViewModel(val firstDayOfWeek:Int,val gridNumColumns:Int):BaseCustomModel
class CustomDatePickerView(context: Context): BaseCustomView<DialogChoiceDateBinding,CustomDatePickerViewModel>(context) {
    private val calendarConstraints: CalendarConstraints? = null
    override fun getViewLayoutId(): Int {
        return R.layout.dialog_choice_date
    }

    override fun getDataVariableId(): Int {
        return BR.vm
    }

    override fun getClickVariableId(): Int {
        return BR.action
    }
}