package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef
const val MODE_SINGLE_DATE = 0
const val MODE_DATE_RANGE = 1
@IntDef(MODE_SINGLE_DATE,MODE_DATE_RANGE)
annotation class PickerSelectionMode {
}