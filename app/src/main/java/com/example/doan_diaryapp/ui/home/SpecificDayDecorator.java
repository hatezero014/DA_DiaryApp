package com.example.doan_diaryapp.ui.home;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.example.doan_diaryapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class SpecificDayDecorator implements DayViewDecorator {

    private final CalendarDay date;
    private final int Color;

    public SpecificDayDecorator(CalendarDay date, int dotColor) {
        this.date = date;
        this.Color = dotColor;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(4, this.Color));
    }
}
