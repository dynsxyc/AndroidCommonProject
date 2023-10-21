/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dyn.base.ui.weight.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.google.android.material.R;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;

/**
 * Data class for loaded {@code R.styleable.MaterialCalendar} and {@code
 * R.styleable.MaterialCalendarItem} attributes.
 */
final class CustomCalendarStyle {

  /**
   * The {@code R.styleable.MaterialCalendarItem} style for days with no unique characteristics from
   * {@code R.styleable.MaterialCalendar_dayStyle}.
   */
  @NonNull final CustomCalendarItemStyle day;
  /**
   * The {@code R.styleable.MaterialCalendarItem} style for selected days from {@code
   * R.styleable.MaterialCalendar_daySelectedStyle}.
   */
  @NonNull final CustomCalendarItemStyle selectedDay;
  /**
   * The {@code R.styleable.MaterialCalendarItem} style for today from {@code
   * R.styleable.MaterialCalendar_dayTodayStyle}.
   */
  @NonNull final CustomCalendarItemStyle todayDay;

  /**
   * The {@code R.styleable.MaterialCalendarItem} style for years with no unique characteristics
   * from {@code R.styleable#MaterialCalendar_yearStyle}.
   */
  @NonNull final CustomCalendarItemStyle year;
  /**
   * The {@code R.styleable.MaterialCalendarItem} style for selected years from {@code
   * R.styleable.MaterialCalendar_yearSelectedStyle}.
   */
  @NonNull final CustomCalendarItemStyle selectedYear;
  /**
   * The {@code R.styleable.MaterialCalendarItem} style for today's year from {@code
   * R.styleable.MaterialCalendar_yearTodayStyle}.
   */
  @NonNull final CustomCalendarItemStyle todayYear;

  @NonNull final CustomCalendarItemStyle invalidDay;

  /**
   * A {@link Paint} for styling days between selected days with {@link
   * R.styleable#MaterialCalendar_rangeFillColor}.
   */
  @NonNull final Paint rangeFill;

  CustomCalendarStyle(@NonNull Context context) {
    int calendarStyle =
        MaterialAttributes.resolveOrThrow(
            context, R.attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName());
    TypedArray calendarAttributes =
        context.obtainStyledAttributes(calendarStyle, R.styleable.MaterialCalendar);

    day =
            CustomCalendarItemStyle.create(
            context, calendarAttributes.getResourceId(R.styleable.MaterialCalendar_dayStyle, 0));
    invalidDay =
            CustomCalendarItemStyle.create(
            context,
            calendarAttributes.getResourceId(R.styleable.MaterialCalendar_dayInvalidStyle, 0));
    selectedDay =
            CustomCalendarItemStyle.create(
            context,
            calendarAttributes.getResourceId(R.styleable.MaterialCalendar_daySelectedStyle, 0));
    todayDay =
            CustomCalendarItemStyle.create(
            context,
            calendarAttributes.getResourceId(R.styleable.MaterialCalendar_dayTodayStyle, 0));
    ColorStateList rangeFillColorList =
        MaterialResources.getColorStateList(
            context, calendarAttributes, R.styleable.MaterialCalendar_rangeFillColor);

    year =
            CustomCalendarItemStyle.create(
            context, calendarAttributes.getResourceId(R.styleable.MaterialCalendar_yearStyle, 0));
    selectedYear =
            CustomCalendarItemStyle.create(
            context,
            calendarAttributes.getResourceId(R.styleable.MaterialCalendar_yearSelectedStyle, 0));
    todayYear =
            CustomCalendarItemStyle.create(
            context,
            calendarAttributes.getResourceId(R.styleable.MaterialCalendar_yearTodayStyle, 0));

    rangeFill = new Paint();
    rangeFill.setColor(rangeFillColorList.getDefaultColor());

    calendarAttributes.recycle();
  }
}
