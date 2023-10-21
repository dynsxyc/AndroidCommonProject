package com.dyn.base.ui.weight.datepicker

import android.view.View
import android.widget.GridView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialCalendar
import com.google.android.material.datepicker.MaterialDatePicker

object BindingCustomDatePicker {
    @JvmStatic
    @BindingAdapter("initDatePickerMonths")
    fun initPickerMonths(recyclerView:RecyclerView,boolean: Boolean){
        val layoutManager: CustomSmoothCalendarLayoutManager =
            object : CustomSmoothCalendarLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false) {
                protected override fun calculateExtraLayoutSpace(state: RecyclerView.State, ints: IntArray) {
                    if (orientation == HORIZONTAL) {
                        ints[0] = recyclerView.width
                        ints[1] = recyclerView.width
                    } else {
                        ints[0] = recyclerView.height
                        ints[1] = recyclerView.height
                    }
                }
            }
        recyclerView.layoutManager = layoutManager
        recyclerView.tag = "MONTHS_VIEW_GROUP_TAG"

//        val monthsPagerAdapter = CustomMonthsPagerAdapter(
//            recyclerView.context,
//            dateSelector,
//            calendarConstraints,
//            dayViewDecorator,
//            object : CustomOnDayClickListener {
//                override fun onDayClick(day: Long) {
//                    if (calendarConstraints.dateValidator.isValid(day)) {
//                        dateSelector.select(day)
//                        for (listener in onSelectionChangedListeners) {
//                            listener.onSelectionChanged(dateSelector.getSelection())
//                        }
//                        // TODO(b/134663744): Look into monthsPager.getAdapter().notifyItemRangeChanged();
//                        recyclerView.adapter?.notifyDataSetChanged()
////                        if (yearSelector != null) {
////                            yearSelector.getAdapter().notifyDataSetChanged()
////                        }
//                    }
//                }
//            })
//        recyclerView.adapter = monthsPagerAdapter

//        val columns: Int =
//            themedContext.getResources().getInteger(R.integer.mtrl_calendar_year_selector_span)
//        yearSelector = root.findViewById<RecyclerView>(R.id.mtrl_calendar_year_selector_frame)
//        if (yearSelector != null) {
//            yearSelector.setHasFixedSize(true)
//            yearSelector.setLayoutManager(
//                GridLayoutManager(themedContext, columns, RecyclerView.VERTICAL, false)
//            )
//            yearSelector.setAdapter(YearGridAdapter(this))
//            yearSelector.addItemDecoration(createItemDecoration())
//        }

//        if (root.findViewById<View>(R.id.month_navigation_fragment_toggle) != null) {
//            addActionsToMonthNavigation(root, monthsPagerAdapter)
//        }

//        if (!MaterialDatePicker.isFullscreen(themedContext)) {
//            PagerSnapHelper().attachToRecyclerView(recyclerView)
//        }
//        recyclerView.scrollToPosition(monthsPagerAdapter.getPosition(current))
    }
    @JvmStatic
    @BindingAdapter("firstDayOfWeek","gridNumColumns")
    fun weekAdapter(daysHeader:GridView,firstDayOfWeek:Int,numColumns:Int){
        ViewCompat.setAccessibilityDelegate(
            daysHeader,
            object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(
                    view: View, accessibilityNodeInfoCompat: AccessibilityNodeInfoCompat
                ) {
                    super.onInitializeAccessibilityNodeInfo(
                        view,
                        accessibilityNodeInfoCompat
                    )
                    // Remove announcing row/col info.
                    accessibilityNodeInfoCompat.setCollectionInfo(null)
                }
            })
//        val firstDayOfWeek: Int = calendarConstraints.getFirstDayOfWeek()
        daysHeader.adapter =if (firstDayOfWeek > 0) CustomDaysOfWeekAdapter(firstDayOfWeek) else CustomDaysOfWeekAdapter()
//        daysHeader.numColumns = earliestMonth.daysInWeek
        daysHeader.numColumns = numColumns
        daysHeader.isEnabled = false
    }
}