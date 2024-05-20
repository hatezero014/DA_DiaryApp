package com.example.doan_diaryapp.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.example.doan_diaryapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SpecificDay implements DayViewDecorator {

    private final Drawable backgroundDrawable;
    private final CalendarDay date;

    public SpecificDay(Context context, CalendarDay date) {
        this.date = date;
        this.backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_circle_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(backgroundDrawable);
    }
}
