package com.dyn.base.ui.weight.datepicker

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.dyn.base.R
import com.google.android.material.datepicker.*
import java.util.*

object MaterialDatePickerManager {

    private var today: Long = 0
    private var nextMonth: Long = 0
    private var janThisYear: Long = 0
    private var decThisYear: Long = 0
    private var oneYearForward: Long = 0
    private var todayPair: Pair<Long, Long>? = null
    private var nextMonthPair: Pair<Long, Long>? = null

    private fun getClearedUtc(): Calendar {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.clear()
        return utc
    }

    private fun initSettings() {
        today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = getClearedUtc()
        calendar.timeInMillis = today
        calendar.roll(Calendar.MONTH, 1)
        nextMonth = calendar.timeInMillis
        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        janThisYear = calendar.timeInMillis
        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        decThisYear = calendar.timeInMillis
        calendar.timeInMillis = today
        calendar.roll(Calendar.YEAR, 1)
        oneYearForward = calendar.timeInMillis
        todayPair = Pair(today, today)
        nextMonthPair = Pair(nextMonth, nextMonth)
    }

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        titleStr: String? = null,
        @PickerSelectionMode selectionModeChoice: Int = MODE_SINGLE_DATE,
        @PickerDefaultSelection selectionChoice: Int = MODE_SELECTION_NONE,
        @PickerInputMode inputModeChoices: Int = INPUT_MODE_CALENDAR,
        @PickerBoundsChoiceMode boundsChoice: Int = BOUNDS_CHOICE_DEFAULT,
        @PickerOpeningChoiceMode openingChoice: Int = OPENING_CHOICE_DEFAULT,
        @PickerValidationChoiceMode validationChoice: Int = VALIDATION_CHOICE_DEFAULT,
        @PickerThemeChoiceMode themeChoice: Int = THEME_MODE_DIALOG,
        complete:(Any?)->Unit
    ) {
        val dialogTheme = resolveOrThrow(context, R.attr.materialCalendarTheme)
        val fullscreenTheme = resolveOrThrow(context, R.attr.materialCalendarFullscreenTheme)
        initSettings()
        val builder =
            setupDateSelectorBuilder(selectionModeChoice, selectionChoice, inputModeChoices)
        val constraintsBuilder =
            setupConstraintsBuilder(boundsChoice, openingChoice, validationChoice)
        when (themeChoice) {
            THEME_MODE_DIALOG -> {
                builder.setTheme(dialogTheme)
            }
            THEME_MODE_FULLSCREEN -> {
                builder.setTheme(fullscreenTheme)
            }
            THEME_MODE_CUSTOM -> {
                builder.setTheme(R.style.ThemeOverlay_Catalog_MaterialCalendar_Custom)
            }
        }
        if (TextUtils.isEmpty(titleStr).not()) {
            builder.setTitleText(titleStr)
        }
        try {
            builder.setCalendarConstraints(constraintsBuilder.build())
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener { selection: Any? ->
                complete(selection)
                Log.i("MaterialDatePicker", "selection->$selection  header ->${picker.headerText}")
            }
            picker.addOnNegativeButtonClickListener { dialog: View? ->
                Log.i("MaterialDatePicker", "The user clicked the cancel button.")
            }
            picker.addOnCancelListener { dialog: DialogInterface? ->
                Log.i("MaterialDatePicker", "The picker was cancelled.")
            }
            picker.show(fragmentManager, picker.toString())
        } catch (e: IllegalArgumentException) {
            Log.i("MaterialDatePicker", "exception->${e.message}")

        }
    }

    private fun setupDateSelectorBuilder(
        @PickerSelectionMode selectionModeChoice: Int, selectionChoice: Int, inputModeChoice: Int
    ): MaterialDatePicker.Builder<*> {
        val inputMode =
            if (inputModeChoice == INPUT_MODE_CALENDAR) MaterialDatePicker.INPUT_MODE_CALENDAR else MaterialDatePicker.INPUT_MODE_TEXT
        return if (selectionModeChoice == MODE_SINGLE_DATE) {
            val builder = MaterialDatePicker.Builder.datePicker()
            if (selectionChoice == MODE_SELECTION_TODAY) {
                builder.setSelection(today)
            } else if (selectionChoice == MODE_SELECTION_NEXT_MONTH) {
                builder.setSelection(nextMonth)
            }
            builder.setInputMode(inputMode)
            builder
        } else {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            if (selectionChoice == MODE_SELECTION_TODAY) {
                builder.setSelection(todayPair)
            } else if (selectionChoice == MODE_SELECTION_NEXT_MONTH) {
                builder.setSelection(nextMonthPair)
            }
            builder.setInputMode(inputMode)
            builder
        }
    }

    private fun setupConstraintsBuilder(
        boundsChoice: Int, openingChoice: Int, validationChoice: Int
    ): CalendarConstraints.Builder {
        val constraintsBuilder = CalendarConstraints.Builder()
        if (boundsChoice == BOUNDS_CHOICE_THIS_YEAR) {
            constraintsBuilder.setStart(janThisYear)
            constraintsBuilder.setEnd(decThisYear)
        } else if (boundsChoice == BOUNDS_CHOICE_WITHIN_1_YEAR) {
            constraintsBuilder.setStart(today)
            constraintsBuilder.setEnd(oneYearForward)
        }
        if (openingChoice == OPENING_CHOICE_TODAY) {
            constraintsBuilder.setOpenAt(today)
        } else if (openingChoice == OPENING_CHOICE_NEXT_1_MOUTH) {
            constraintsBuilder.setOpenAt(nextMonth)
        }
        if (validationChoice == VALIDATION_CHOICE_TODAY_ONWARD) {
            constraintsBuilder.setValidator(DateValidatorPointForward.now())
        } else if (validationChoice == VALIDATION_CHOICE_WEEKDAYS) {
            constraintsBuilder.setValidator(DateValidatorWeekdays())
        } else if (validationChoice == VALIDATION_CHOICE_LAST_2_WEEKS) {
            val lowerBoundCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            lowerBoundCalendar.add(Calendar.DAY_OF_MONTH, -14)
            val lowerBound = lowerBoundCalendar.timeInMillis
            val validators: MutableList<CalendarConstraints.DateValidator> = ArrayList()
            validators.add(DateValidatorPointForward.from(lowerBound))
            validators.add(DateValidatorWeekdays())
            constraintsBuilder.setValidator(CompositeDateValidator.allOf(validators))
        } else if (validationChoice == VALIDATION_CHOICE_MULTIPLE_RANGE) {
            val validatorsMultple: MutableList<CalendarConstraints.DateValidator> = ArrayList()
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = today
            utc[Calendar.DATE] = 10
            val pointBackward = DateValidatorPointBackward.before(utc.timeInMillis)
            utc[Calendar.DATE] = 20
            val validatorsComposite: MutableList<CalendarConstraints.DateValidator> = ArrayList()
            val pointForwardComposite = DateValidatorPointForward.from(utc.timeInMillis)
            utc[Calendar.DATE] = 26
            val pointBackwardComposite = DateValidatorPointBackward.before(utc.timeInMillis)
            validatorsComposite.add(pointForwardComposite)
            validatorsComposite.add(pointBackwardComposite)
            val compositeDateValidator = CompositeDateValidator.allOf(validatorsComposite)
            validatorsMultple.add(pointBackward)
            validatorsMultple.add(compositeDateValidator)
            constraintsBuilder.setValidator(CompositeDateValidator.anyOf(validatorsMultple))
        }
        return constraintsBuilder
    }

    private fun resolveOrThrow(context: Context, @AttrRes attributeResId: Int): Int {
        val typedValue = TypedValue()
        if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue.data
        }
        throw IllegalArgumentException(context.resources.getResourceName(attributeResId))
    }
}