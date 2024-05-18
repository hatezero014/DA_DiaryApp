package com.example.doan_diaryapp.ui.home;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SpecificDayDecorator implements DayViewDecorator {

    private final CalendarDay date;

    public SpecificDayDecorator(CalendarDay date) {
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED)); // Đổi màu số ngày thành màu đỏ
    }
}
