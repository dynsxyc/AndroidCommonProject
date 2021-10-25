package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef
const val INPUT_MODE_CALENDAR = 0
const val INPUT_MODE_TEXT_INPUT = 1

@IntDef(INPUT_MODE_CALENDAR,INPUT_MODE_TEXT_INPUT)
annotation class PickerInputMode {
}