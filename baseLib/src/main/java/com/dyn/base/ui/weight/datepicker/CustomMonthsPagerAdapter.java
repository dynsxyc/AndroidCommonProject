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

import static com.blankj.utilcode.util.SizeUtils.dp2px;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

import com.google.android.material.R;

class CustomMonthsPagerAdapter extends RecyclerView.Adapter<CustomMonthsPagerAdapter.ViewHolder> {

    @NonNull
    private final CustomCalendarConstraints calendarConstraints;
    private final DateSelector<?> dateSelector;
    @Nullable
    private final CustomDayViewDecorator dayViewDecorator;
    private final CustomOnDayClickListener onDayClickListener;
    private final int itemHeight;

    CustomMonthsPagerAdapter(
            @NonNull Context context,
            DateSelector<?> dateSelector,
            @NonNull CustomCalendarConstraints calendarConstraints,
            @Nullable CustomDayViewDecorator dayViewDecorator,
            CustomOnDayClickListener onDayClickListener) {
        CustomMonth firstPage = calendarConstraints.getStart();
        CustomMonth lastPage = calendarConstraints.getEnd();
        CustomMonth currentPage = calendarConstraints.getOpenAt();

        if (firstPage.compareTo(currentPage) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        }
        if (currentPage.compareTo(lastPage) > 0) {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }

        int daysHeight = CustomMonthAdapter.MAXIMUM_WEEKS * dp2px(40f);
        int labelHeight =  dp2px(40f);              //MaterialDatePicker.isFullscreen(context) ? dp2px(40f) : 0;

        this.itemHeight = daysHeight + labelHeight;
        this.calendarConstraints = calendarConstraints;
        this.dateSelector = dateSelector;
        this.dayViewDecorator = dayViewDecorator;
        this.onDayClickListener = onDayClickListener;
        setHasStableIds(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView monthTitle;
        final CustomMaterialCalendarGridView monthGrid;

        ViewHolder(@NonNull LinearLayout container, boolean showLabel) {
            super(container);
            monthTitle = container.findViewById(R.id.month_title);
            ViewCompat.setAccessibilityHeading(monthTitle, true);
            monthGrid = container.findViewById(R.id.month_grid);
            if (!showLabel) {
                monthTitle.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LinearLayout container =
                (LinearLayout)
                        LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.mtrl_calendar_month_labeled, viewGroup, false);

//        if (MaterialDatePicker.isFullscreen(viewGroup.getContext())) {
            container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, itemHeight));
            return new ViewHolder(container, /* showLabel= */ true);
//        } else {
//            return new ViewHolder(container, /* showLabel= */ false);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomMonthsPagerAdapter.ViewHolder viewHolder, int position) {
        CustomMonth month = calendarConstraints.getStart().monthsLater(position);
        viewHolder.monthTitle.setText(month.getLongName());
        final CustomMaterialCalendarGridView monthGrid = viewHolder.monthGrid.findViewById(R.id.month_grid);

        if (monthGrid.getAdapter() != null && month.equals(monthGrid.getAdapter().month)) {
            monthGrid.invalidate();
            monthGrid.getAdapter().updateSelectedStates(monthGrid);
        } else {
            CustomMonthAdapter monthAdapter =
                    new CustomMonthAdapter(month, dateSelector, calendarConstraints, dayViewDecorator);
            monthGrid.setNumColumns(month.daysInWeek);
            monthGrid.setAdapter(monthAdapter);
        }

        monthGrid.setOnItemClickListener(
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (monthGrid.getAdapter().withinMonth(position)) {
                            onDayClickListener.onDayClick(monthGrid.getAdapter().getItem(position));
                        }
                    }
                });
    }

    @Override
    public long getItemId(int position) {
        return calendarConstraints.getStart().monthsLater(position).getStableId();
    }

    @Override
    public int getItemCount() {
        return calendarConstraints.getMonthSpan();
    }

    @NonNull
    CharSequence getPageTitle(int position) {
        return getPageMonth(position).getLongName();
    }

    @NonNull
    CustomMonth getPageMonth(int position) {
        return calendarConstraints.getStart().monthsLater(position);
    }

    int getPosition(@NonNull CustomMonth month) {
        return calendarConstraints.getStart().monthsUntil(month);
    }
}
