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
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doan_diaryapp.ActivityNam;
import com.example.doan_diaryapp.Adapter.EntryAdapter;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.databinding.FragmentMonthBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;



public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    EntryService entryService;
    private EntryAdapter mAdapter;
    private EntryService mEntryService;
    private ListView mListView;
    private MaterialCalendarView calendarView;
    String[] Diary;


    private void updateView() {
        View rootView = getView();
        if (rootView != null) {
            setCardViewDate(rootView);
            ButtonAddMonth(rootView);
            ListViewDay(rootView);
            ShowDiary(rootView);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        entryService=new EntryService(getContext());
        setCardViewDate(view);
        ButtonAddMonth(view);
        ListViewDay(view);
        ShowDiary(view);

        Calendar calendar = Calendar.getInstance();
        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.from(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

        return view;
    }


    private void ShowDiary(View view) {
        entryService = new EntryService(getContext());
        Diary = new String[0];
        Diary = entryService.getEntries();
        for (String date : Diary) {
            String[] parts = date.split("-");
            String d = parts[0];
            String m = parts[1];
            String y = parts[2];
            int intDay = Integer.parseInt(d);
            int intMonth = Integer.parseInt(m);
            int intYear = Integer.parseInt(y);
            calendarView = view.findViewById(R.id.calendarView);
            CalendarDay specificDate = CalendarDay.from(intYear, intMonth, intDay);
            SpecificDayDecorator specificDayDecorator = new SpecificDayDecorator(specificDate);
            calendarView.addDecorator(specificDayDecorator);
        }
    }


    private void ListViewDay(View view)
    {
        mListView = view.findViewById(R.id.list_of_day);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewDate = view.findViewById(R.id.textViewID);
                if (textViewDate.length()!=0) {
                    String dateText = textViewDate.getText().toString();
                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                    intent.putExtra("Date", dateText);
                    startActivity(intent);
                }
            }
        });

    }


    private void setCardViewDate(View view)
    {

        TextView textView= view.findViewById(R.id.textView);
        mListView=view.findViewById(R.id.list_of_day);
        mEntryService = new EntryService(getContext());

        String time = String.format(Locale.ENGLISH, "%02d-%02d-%04d", dayOfMonth, month+1, year);
        List<Entry> entryList = mEntryService.getEntriesFromDatabase(time);
        mAdapter = new EntryAdapter(getContext(), entryList);
        mListView.setAdapter(mAdapter);

        if (entryList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {

            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dayOfMonth = date.getDay();
                month = date.getMonth()-1;
                year = date.getYear();
                String time = String.format(Locale.ENGLISH, "%02d-%02d-%04d", dayOfMonth, month+1, year);
                List<Entry> entryList = mEntryService.getEntriesFromDatabase(time);

                if (entryList.size() == 0) {
                    textView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
                mAdapter.clear();
                mAdapter.addAll(entryList);
                mAdapter.notifyDataSetChanged();
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
                    Calendar calendar = Calendar.getInstance();
                    /*int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);*/
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                    intent.putExtra("Date", String.format(Locale.ENGLISH,
                            "%02d:%02d:%02d %02d-%02d-%04d", hour,minute,second,dayOfMonth, month + 1, year));
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
    public void onResume() {
        super.onResume();
        updateView();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}