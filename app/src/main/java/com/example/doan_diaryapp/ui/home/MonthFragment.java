package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.doan_diaryapp.ActivityNam;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.databinding.FragmentMonthBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        setCardViewDate(view);
        ButtonAddMonth(view);
        return view;
    }



    private void setCardViewDate(View view)
    {
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView selectedDateTextView = view.findViewById(R.id.CardViewDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy", new Locale("vi", "VN"));
        String currentDate = sdf.format(calendar.getTime());
        selectedDateTextView.setText(currentDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int d) {
                dayOfMonth=d; month=m; year=y;
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(y, m, d);
                String selectedDate = sdf.format(selectedCalendar.getTime());
                selectedDateTextView.setText(selectedDate);
            }
        });
    }



    private void ButtonAddMonth(View view)
    {
        FloatingActionButton buttonAddMonth = view.findViewById(R.id.ButtonAddMonth);
        buttonAddMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckDate(dayOfMonth, month, year)) {
                    showAlertDialog();
                } else {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("Date", String.format(Locale.ENGLISH,
                        "%02d-%02d-%04d", dayOfMonth, month+1, year));
                startActivity(intent);
                }
            }
        });

    }

    private Boolean CheckDate(int dayOfMonth,int month,int year) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        if (year<y)  return false;
        if (year==y && month<m)  return false;
        if (year==y && month==m && dayOfMonth<=d)  return false;
        return true;
    }


    private void showAlertDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.month_alert_title)
                .setMessage(R.string.month_alert_message)
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}