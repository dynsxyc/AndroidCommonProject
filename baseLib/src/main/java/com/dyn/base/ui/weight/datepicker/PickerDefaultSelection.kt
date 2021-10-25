package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef

const val MODE_SELECTION_NONE = 0
const val MODE_SELECTION_TODAY = 1
const val MODE_SELECTION_NEXT_MONTH = 2

@IntDef(MODE_SELECTION_NONE, MODE_SELECTION_TODAY, MODE_SELECTION_NEXT_MONTH)
annotation class PickerDefaultSelection {
}