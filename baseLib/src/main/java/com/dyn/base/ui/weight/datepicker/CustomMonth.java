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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** Contains convenience operations for a month within a specific year. */
final class CustomMonth implements Comparable<CustomMonth>, Parcelable {

  /** The acceptable int values for month when using {@link CustomMonth#create(int, int)} */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({
    Calendar.JANUARY,
    Calendar.FEBRUARY,
    Calendar.MARCH,
    Calendar.APRIL,
    Calendar.MAY,
    Calendar.JUNE,
    Calendar.JULY,
    Calendar.AUGUST,
    Calendar.SEPTEMBER,
    Calendar.OCTOBER,
    Calendar.NOVEMBER,
    Calendar.DECEMBER
  })
  @interface Months {}

  @NonNull private final Calendar firstOfMonth;
  @Months final int month;
  final int year;
  final int daysInWeek;
  final int daysInMonth;
  final long timeInMillis;

  @Nullable private String longName;

  private CustomMonth(@NonNull Calendar rawCalendar) {
    rawCalendar.set(Calendar.DAY_OF_MONTH, 1);
    firstOfMonth = CustomUtcDates.getDayCopy(rawCalendar);
    month = firstOfMonth.get(Calendar.MONTH);
    year = firstOfMonth.get(Calendar.YEAR);
    daysInWeek = firstOfMonth.getMaximum(Calendar.DAY_OF_WEEK);
    daysInMonth = firstOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
    timeInMillis = firstOfMonth.getTimeInMillis();
  }

  /**
   * Creates an instance of Month that contains the provided {@code timeInMillis} where {@code
   * timeInMillis} is in milliseconds since 00:00:00 January 1, 1970, UTC.
   */
  @NonNull
  static CustomMonth create(long timeInMillis) {
    Calendar calendar = CustomUtcDates.getUtcCalendar();
    calendar.setTimeInMillis(timeInMillis);
    return new CustomMonth(calendar);
  }

  /**
   * Creates an instance of Month with the given parameters backed by a {@link Calendar}.
   *
   * @param year The year
   * @param month The 0-index based month. Use {@link Calendar} constants (e.g., {@link
   *     Calendar#JANUARY}
   * @return A Month object backed by a new {@link Calendar} instance
   */
  @NonNull
  static CustomMonth create(int year, @Months int month) {
    Calendar calendar = CustomUtcDates.getUtcCalendar();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    return new CustomMonth(calendar);
  }

  /**
   * Returns the {@link CustomMonth} that contains the first moment in current month in the default
   * timezone (as per {@link Calendar#getInstance()}.
   */
  @NonNull
  static CustomMonth current() {
    return new CustomMonth(CustomUtcDates.getTodayCalendar());
  }

  int daysFromStartOfWeekToFirstOfMonth(int firstDayOfWeek) {
    int difference =
        firstOfMonth.get(Calendar.DAY_OF_WEEK)
            - (firstDayOfWeek > 0 ? firstDayOfWeek : firstOfMonth.getFirstDayOfWeek());
    if (difference < 0) {
      difference = difference + daysInWeek;
    }
    return difference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomMonth)) {
      return false;
    }
    CustomMonth that = (CustomMonth) o;
    return month == that.month && year == that.year;
  }

  @Override
  public int hashCode() {
    Object[] hashedFields = {month, year};
    return Arrays.hashCode(hashedFields);
  }

  @Override
  public int compareTo(@NonNull CustomMonth other) {
    return firstOfMonth.compareTo(other.firstOfMonth);
  }

  /**
   * Returns the number of months from this Month to the provided Month.
   *
   * <p>0 when {@code this.compareTo(other)} is 0. Negative when {@code this.compareTo(other)} is
   * negative.
   *
   * @throws IllegalArgumentException when {@link Calendar#getInstance()} is not an instance of
   *     {@link GregorianCalendar}
   */
  int monthsUntil(@NonNull CustomMonth other) {
    if (firstOfMonth instanceof GregorianCalendar) {
      return (other.year - year) * 12 + (other.month - month);
    } else {
      throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }
  }

  long getStableId() {
    return firstOfMonth.getTimeInMillis();
  }

  /**
   * Gets a long for the specific day within the instance's month and year.
   *
   * <p>This method only guarantees validity with respect to {@link Calendar#isLenient()}.
   *
   * @param day The desired day within this month and year
   * @return A long representing a time in milliseconds for the given day within the specified month
   *     and year
   */
  long getDay(int day) {
    Calendar dayCalendar = CustomUtcDates.getDayCopy(firstOfMonth);
    dayCalendar.set(Calendar.DAY_OF_MONTH, day);
    return dayCalendar.getTimeInMillis();
  }

  int getDayOfMonth(long date) {
    Calendar dayCalendar = CustomUtcDates.getDayCopy(firstOfMonth);
    dayCalendar.setTimeInMillis(date);
    return dayCalendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns a {@link CustomMonth} {@code months} months after this
   * instance.
   */
  @NonNull
  CustomMonth monthsLater(int months) {
    Calendar laterMonth = CustomUtcDates.getDayCopy(firstOfMonth);
    laterMonth.add(Calendar.MONTH, months);
    return new CustomMonth(laterMonth);
  }

  /** Returns a localized String representation of the month name and year. */
  @NonNull
  String getLongName() {
    if (longName == null) {
      longName = CustomDateStrings.getYearMonth(firstOfMonth.getTimeInMillis());
    }
    return longName;
  }

  /* Parcelable interface */

  /** {@link Creator} */
  public static final Creator<CustomMonth> CREATOR =
      new Creator<CustomMonth>() {
        @NonNull
        @Override
        public CustomMonth createFromParcel(@NonNull Parcel source) {
          int year = source.readInt();
          int month = source.readInt();
          return CustomMonth.create(year, month);
        }

        @NonNull
        @Override
        public CustomMonth[] newArray(int size) {
          return new CustomMonth[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeInt(year);
    dest.writeInt(month);
  }
}
