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

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.util.ObjectsCompat;


import java.util.Arrays;
import java.util.Calendar;

/**
 * Used to limit the display range of the calendar and set an openAt month.
 *
 * <p>Implements {@link Parcelable} in order to maintain the {@code CalendarConstraints} across
 * device configuration changes. Parcelable breaks when passed between processes.
 */
public final class CustomCalendarConstraints implements Parcelable {

  @NonNull private final CustomMonth start;
  @NonNull private final CustomMonth end;
  @NonNull private final CustomDateValidator validator;

  @Nullable private CustomMonth openAt;
  private final int firstDayOfWeek;

  private final int yearSpan;
  private final int monthSpan;

  /**
   * Used to determine whether calendar days are enabled.
   *
   * <p>Extends {@link Parcelable} in order to maintain the {@code DateValidator} across device
   * configuration changes. Parcelable breaks when passed between processes.
   */
  public interface CustomDateValidator extends Parcelable {

    /** Returns true if the provided {@code date} is enabled. */
    boolean isValid(long date);
  }

  private CustomCalendarConstraints(
      @NonNull CustomMonth start,
      @NonNull CustomMonth end,
      @NonNull CustomDateValidator validator,
      @Nullable CustomMonth openAt,
      int firstDayOfWeek) {
    this.start = start;
    this.end = end;
    this.openAt = openAt;
    this.firstDayOfWeek = firstDayOfWeek;
    this.validator = validator;
    if (openAt != null && start.compareTo(openAt) > 0) {
      throw new IllegalArgumentException("start Month cannot be after current Month");
    }
    if (openAt != null && openAt.compareTo(end) > 0) {
      throw new IllegalArgumentException("current Month cannot be after end Month");
    }
    if (firstDayOfWeek < 0
        || firstDayOfWeek > CustomUtcDates.getUtcCalendar().getMaximum(Calendar.DAY_OF_WEEK)) {
      throw new IllegalArgumentException("firstDayOfWeek is not valid");
    }
    monthSpan = start.monthsUntil(end) + 1;
    yearSpan = end.year - start.year + 1;
  }

  boolean isWithinBounds(long date) {
    return start.getDay(1) <= date && date <= end.getDay(end.daysInMonth);
  }

  /**
   * Returns the {@link CustomDateValidator} that determines whether a date can be clicked and selected.
   */
  public CustomDateValidator getDateValidator() {
    return validator;
  }

  /** Returns the earliest month allowed by this set of bounds. */
  @NonNull
  CustomMonth getStart() {
    return start;
  }

  /** Returns the latest month allowed by this set of bounds. */
  @NonNull
  CustomMonth getEnd() {
    return end;
  }

  /** Returns the openAt month within this set of bounds. */
  @Nullable
  CustomMonth getOpenAt() {
    return openAt;
  }

  /** Sets the openAt month. */
  void setOpenAt(@Nullable CustomMonth openAt) {
    this.openAt = openAt;
  }

  /** Returns the firstDayOfWeek. */
  int getFirstDayOfWeek() {
    return firstDayOfWeek;
  }

  /**
   * Returns the total number of {@link Calendar#MONTH} included in {@code start} to
   * {@code end}.
   */
  int getMonthSpan() {
    return monthSpan;
  }

  /**
   * Returns the total number of {@link Calendar#YEAR} included in {@code start} to {@code
   * end}.
   */
  int getYearSpan() {
    return yearSpan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomCalendarConstraints)) {
      return false;
    }
    CustomCalendarConstraints that = (CustomCalendarConstraints) o;
    return start.equals(that.start)
        && end.equals(that.end)
        && ObjectsCompat.equals(openAt, that.openAt)
        && firstDayOfWeek == that.firstDayOfWeek
        && validator.equals(that.validator);
  }

  @Override
  public int hashCode() {
    Object[] hashedFields = {start, end, openAt, firstDayOfWeek, validator};
    return Arrays.hashCode(hashedFields);
  }

  /* Parcelable interface */

  /** {@link Creator} */
  public static final Creator<CustomCalendarConstraints> CREATOR =
      new Creator<CustomCalendarConstraints>() {
        @NonNull
        @Override
        public CustomCalendarConstraints createFromParcel(@NonNull Parcel source) {
          CustomMonth start = source.readParcelable(CustomMonth.class.getClassLoader());
          CustomMonth end = source.readParcelable(CustomMonth.class.getClassLoader());
          CustomMonth openAt = source.readParcelable(CustomMonth.class.getClassLoader());
          CustomDateValidator validator = source.readParcelable(CustomDateValidator.class.getClassLoader());
          int firstDayOfWeek = source.readInt();
          return new CustomCalendarConstraints(start, end, validator, openAt, firstDayOfWeek);
        }

        @NonNull
        @Override
        public CustomCalendarConstraints[] newArray(int size) {
          return new CustomCalendarConstraints[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(start, /* parcelableFlags= */ 0);
    dest.writeParcelable(end, /* parcelableFlags= */ 0);
    dest.writeParcelable(openAt, /* parcelableFlags= */ 0);
    dest.writeParcelable(validator, /* parcelableFlags = */ 0);
    dest.writeInt(firstDayOfWeek);
  }

  /**
   * Returns the given month if it's within the constraints or the closest bound if it's outside.
   */
  CustomMonth clamp(CustomMonth month) {
    if (month.compareTo(start) < 0) {
      return start;
    }

    if (month.compareTo(end) > 0) {
      return end;
    }

    return month;
  }

  /** Builder for {@link CustomCalendarConstraints}. */
  public static final class Builder {

    /**
     * Default UTC timeInMilliseconds for the first selectable month unless {@link Builder#setStart}
     * is called. Set to January, 1900.
     */
    static final long DEFAULT_START =
        CustomUtcDates.canonicalYearMonthDay(CustomMonth.create(1900, Calendar.JANUARY).timeInMillis);
    /**
     * Default UTC timeInMilliseconds for the last selectable month unless {@link Builder#setEnd} is
     * called. Set to December, 2100.
     */
    static final long DEFAULT_END =
        CustomUtcDates.canonicalYearMonthDay(CustomMonth.create(2100, Calendar.DECEMBER).timeInMillis);

    private static final String DEEP_COPY_VALIDATOR_KEY = "DEEP_COPY_VALIDATOR_KEY";

    private long start = DEFAULT_START;
    private long end = DEFAULT_END;
    private Long openAt;
    private int firstDayOfWeek;
    private CustomDateValidator validator = CustomDateValidatorPointForward.from(Long.MIN_VALUE);

    public Builder() {}

    Builder(@NonNull CustomCalendarConstraints clone) {
      start = clone.start.timeInMillis;
      end = clone.end.timeInMillis;
      openAt = clone.openAt.timeInMillis;
      firstDayOfWeek = clone.firstDayOfWeek;
      validator = clone.validator;
    }

    /**
     * A UTC timeInMilliseconds contained within the earliest month the calendar will page to.
     * Defaults January, 1900.
     *
     * <p>If you have access to java.time in Java 8, you can obtain a long using {@code
     * java.time.ZonedDateTime}.
     *
     * <pre>{@code
     * LocalDateTime local = LocalDateTime.of(year, month, 1, 0, 0);
     * local.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli();
     * }</pre>
     *
     * <p>If you don't have access to java.time in Java 8, you can obtain this value using a {@code
     * java.util.Calendar} instance from the UTC timezone.
     *
     * <pre>{@code
     * Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * c.set(year, month, 1);
     * c.getTimeInMillis();
     * }</pre>
     */
    @NonNull
    public Builder setStart(long month) {
      start = month;
      return this;
    }

    /**
     * A UTC timeInMilliseconds contained within the latest month the calendar will page to.
     * Defaults December, 2100.
     *
     * <p>If you have access to java.time in Java 8, you can obtain a long using {@code
     * java.time.ZonedDateTime}.
     *
     * <pre>{@code
     * LocalDateTime local = LocalDateTime.of(year, month, 1, 0, 0);
     * local.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli();
     * }</pre>
     *
     * <p>If you don't have access to java.time in Java 8, you can obtain this value using a {@code
     * java.util.Calendar} instance from the UTC timezone.
     *
     * <pre>{@code
     * Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * c.set(year, month, 1);
     * c.getTimeInMillis();
     * }</pre>
     */
    @NonNull
    public Builder setEnd(long month) {
      end = month;
      return this;
    }

    /**
     * A UTC timeInMilliseconds contained within the month the calendar should openAt. Defaults to
     * the month containing today if within bounds; otherwise, defaults to the starting month.
     *
     * <p>If you have access to java.time in Java 8, you can obtain a long using {@code
     * java.time.ZonedDateTime}.
     *
     * <pre>{@code
     * LocalDateTime local = LocalDateTime.of(year, month, 1, 0, 0);
     * local.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli();
     * }</pre>
     *
     * <p>If you don't have access to java.time in Java 8, you can obtain this value using a {@code
     * java.util.Calendar} instance from the UTC timezone.
     *
     * <pre>{@code
     * Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * c.set(year, month, 1);
     * c.getTimeInMillis();
     * }</pre>
     */
    @NonNull
    public Builder setOpenAt(long month) {
      openAt = month;
      return this;
    }

    /**
     * Sets what the first day of the week is; e.g., <code>Calendar.SUNDAY</code> in the U.S.,
     * <code>Calendar.MONDAY</code> in France.
     *
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @NonNull
    public Builder setFirstDayOfWeek(int firstDayOfWeek) {
      this.firstDayOfWeek = firstDayOfWeek;
      return this;
    }

    /**
     * Limits valid dates to those for which {@link CustomDateValidator#isValid(long)} is true. Defaults
     * to all dates as valid.
     */
    @NonNull
    public Builder setValidator(@NonNull CustomDateValidator validator) {
      this.validator = validator;
      return this;
    }

    /** Builds the {@link CalendarConstraints} object using the set parameters or defaults. */
    @NonNull
    public CustomCalendarConstraints build() {
      Bundle deepCopyBundle = new Bundle();
      deepCopyBundle.putParcelable(DEEP_COPY_VALIDATOR_KEY, validator);
      return new CustomCalendarConstraints(
              CustomMonth.create(start),
              CustomMonth.create(end),
          (CustomDateValidator) deepCopyBundle.getParcelable(DEEP_COPY_VALIDATOR_KEY),
          openAt == null ? null : CustomMonth.create(openAt),
          firstDayOfWeek);
    }
  }
}
