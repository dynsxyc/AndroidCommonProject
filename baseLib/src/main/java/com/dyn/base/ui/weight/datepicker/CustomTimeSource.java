/*
 * Copyright 2020 The Android Open Source Project
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

import androidx.annotation.Nullable;


import java.util.Calendar;
import java.util.TimeZone;

/** Provider for the current date and time. */
class CustomTimeSource {

  private static final CustomTimeSource SYSTEM_TIME_SOURCE = new CustomTimeSource(null, null);

  @Nullable private final Long fixedTimeMs;

  @Nullable private final TimeZone fixedTimeZone;

  private CustomTimeSource(@Nullable final Long fixedTimeMs, @Nullable final TimeZone fixedTimeZone) {
    this.fixedTimeMs = fixedTimeMs;
    this.fixedTimeZone = fixedTimeZone;
  }

  /**
   * A time source that returns the current time using the best available system clock.
   *
   * <p>For testability, rather than calling this method directly, most classes should have an
   * instance of {@code TimeSource} <i>provided</i> to them, for example by dependency injection.
   */
  static CustomTimeSource system() {
    return SYSTEM_TIME_SOURCE;
  }

  /**
   * Obtains a {@code TimeSource} that always returns the same time in the specified timezone.
   *
   * <p>This clock simply returns the specified instant. As such, it is not a clock in the
   * conventional sense. The main use case for this is in testing, where the fixed clock ensures
   * tests are not dependent on the current clock.
   *
   * <p>The returned implementation is immutable, thread-safe and {@code Serializable}.
   *
   * @param epochMs the time in UTC milliseconds from the epoch.
   * @param timeZone the timezone to use to convert the date-time. If this value is null, the host
   *     device's timezone will be used.
   */
  static CustomTimeSource fixed(long epochMs, @Nullable TimeZone timeZone) {
    return new CustomTimeSource(epochMs, timeZone);
  }

  /**
   * Obtains a {@code TimeSource} that always returns the same time in the system timezone.
   *
   * <p>This clock simply returns the specified instant. As such, it is not a clock in the
   * conventional sense. The main use case for this is in testing, where the fixed clock ensures
   * tests are not dependent on the current clock.
   *
   * <p>The returned implementation is immutable, thread-safe and {@code Serializable}.
   *
   * @param epochMs the time in UTC milliseconds from the epoch.
   */
  static CustomTimeSource fixed(long epochMs) {
    return new CustomTimeSource(epochMs, null);
  }

  /** Returns a {@code Calendar} according to this time source. */
  Calendar now() {
    return now(fixedTimeZone);
  }

  /**
   * Returns a {@code Calendar} according to this time source in the specified timezone.
   *
   * @param timeZone the timezone to use to convert the date-time. If this value is null, the host
   *     device's timezone will be used.
   */
  Calendar now(@Nullable TimeZone timeZone) {
    Calendar calendar = timeZone == null ? Calendar.getInstance() : Calendar.getInstance(timeZone);
    if (fixedTimeMs != null) {
      calendar.setTimeInMillis(fixedTimeMs);
    }

    return calendar;
  }
}
