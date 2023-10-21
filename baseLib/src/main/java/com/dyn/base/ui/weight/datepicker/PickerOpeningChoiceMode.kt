package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef
const val OPENING_CHOICE_DEFAULT = 0
const val OPENING_CHOICE_TODAY = 1
const val OPENING_CHOICE_NEXT_1_MOUTH = 2
@IntDef(OPENING_CHOICE_DEFAULT,OPENING_CHOICE_TODAY,OPENING_CHOICE_NEXT_1_MOUTH)
annotation class PickerOpeningChoiceMode {//打开默认显示月份位置
}