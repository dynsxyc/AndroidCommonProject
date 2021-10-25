package com.dyn.base.ui.weight.datepicker

import androidx.annotation.IntDef

const val THEME_MODE_DIALOG = 0
const val THEME_MODE_FULLSCREEN = 1
const val THEME_MODE_CUSTOM = 2

@IntDef(THEME_MODE_DIALOG, THEME_MODE_FULLSCREEN, THEME_MODE_CUSTOM)
annotation class PickerThemeChoiceMode {
}