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

import androidx.annotation.NonNull;


import java.util.Arrays;

/**
 * A {@link CustomCalendarConstraints.CustomDateValidator} that enables dates from a given point forward.
 * Defaults to the current moment in device time forward using {@link
 * CustomDateValidatorPointForward#now()}, but can be set to any point, as UTC milliseconds, using {@link
 * CustomDateValidatorPointForward#from(long)}.
 */
public class CustomDateValidatorPointForward implements CustomCalendarConstraints.CustomDateValidator {

  private final long point;

  private CustomDateValidatorPointForward(long point) {
    this.point = point;
  }

  /**
   * Returns a {@link CustomCalendarConstraints.CustomDateValidator} which enables days from {@code point}, in
   * UTC milliseconds, forward.
   */
  @NonNull
  public static CustomDateValidatorPointForward from(long point) {
    return new CustomDateValidatorPointForward(point);
  }

  /**
   * Returns a {@link CustomCalendarConstraints.CustomDateValidator} enabled from the current moment in device
   * time forward.
   */
  @NonNull
  public static CustomDateValidatorPointForward now() {
    return from(CustomUtcDates.getTodayCalendar().getTimeInMillis());
  }

  /** Part of {@link Parcelable} requirements. Do not use. */
  public static final Creator<CustomDateValidatorPointForward> CREATOR =
      new Creator<CustomDateValidatorPointForward>() {
        @NonNull
        @Override
        public CustomDateValidatorPointForward createFromParcel(@NonNull Parcel source) {
          return new CustomDateValidatorPointForward(source.readLong());
        }

        @NonNull
        @Override
        public CustomDateValidatorPointForward[] newArray(int size) {
          return new CustomDateValidatorPointForward[size];
        }
      };

  @Override
  public boolean isValid(long date) {
    return date >= point;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeLong(point);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomDateValidatorPointForward)) {
      return false;
    }
    CustomDateValidatorPointForward that = (CustomDateValidatorPointForward) o;
    return point == that.point;
  }

  @Override
  public int hashCode() {
    Object[] hashedFields = {point};
    return Arrays.hashCode(hashedFields);
  }
}
