package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef
const val BOUNDS_CHOICE_DEFAULT = 0
const val BOUNDS_CHOICE_THIS_YEAR = 1
const val BOUNDS_CHOICE_WITHIN_1_YEAR = 2
@IntDef(BOUNDS_CHOICE_DEFAULT, BOUNDS_CHOICE_THIS_YEAR, BOUNDS_CHOICE_WITHIN_1_YEAR)
annotation class PickerBoundsChoiceMode {
}