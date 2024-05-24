package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doan_diaryapp.ActivityNam;
import com.example.doan_diaryapp.Adapter.EntryAdapter;
import com.example.doan_diaryapp.MainActivity;
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
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private EntryAdapter mAdapter;
    private EntryService mEntryService;
    private ListView mListView;
    private MaterialCalendarView calendarView;
    String[] Diary;

    private void updateView() {
        View rootView = getView();
        setCardViewDate(rootView);
        ListViewDay(rootView);
        ShowDiary(rootView);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        setCardViewDate(view);
        ListViewDay(view);
        ShowDiary(view);

        Calendar calendar = Calendar.getInstance();
        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.from(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

        return view;
    }



    private void ButtonAddDay()
    {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckDate(dayOfMonth,month,year))
                    {
                        showAlertDialog();

                    } else {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        Intent intent = new Intent(getContext(), RecordActivity.class);
                        intent.putExtra("Date", String.format(Locale.ENGLISH,
                                "%02d:%02d:%02d %02d-%02d-%04d", hour, minute, second, dayOfMonth, month + 1, year));
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void showAlertDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.month_alert_title)
                .setMessage(R.string.month_alert_message)
                .setPositiveButton("OK", null);
        builder.create().show();
    }


    private void ButtonAddDay1()
    {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    Intent intent = new Intent(getContext(), RecordActivity.class);
                    intent.putExtra("Date", String.format(Locale.ENGLISH,
                            "%02d:%02d:%02d %02d-%02d-%04d", hour, minute, second, dayOfMonth, month + 1, year));
                    startActivity(intent);
                }
            });
        }
    }


    private void ShowDiary(View view) {
        mEntryService = new EntryService(getContext());
        Diary = new String[0];
        Diary = mEntryService.getEntries();
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.removeDecorators();

        CalendarDay today = CalendarDay.today();
        SpecificDay todayDecorator = new SpecificDay(getContext(), today);
        calendarView.addDecorator(todayDecorator);


        for (String date : Diary) {
            String[] parts = date.split("-");
            String d = parts[0];
            String m = parts[1];
            String y = parts[2];
            int intDay = Integer.parseInt(d);
            int intMonth = Integer.parseInt(m);
            int intYear = Integer.parseInt(y);
            CalendarDay specificDate = CalendarDay.from(intYear, intMonth, intDay);
            int dotColor = ContextCompat.getColor(getContext(), R.color.md_theme_primary);
            SpecificDayDecorator specificDayDecorator = new SpecificDayDecorator(specificDate, dotColor);
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

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textViewDate = view.findViewById(R.id.textViewID);
                if (textViewDate.length() != 0) {
                    showAlertDialog(textViewDate.getText().toString(),view);
                    return true;
                }
                return false;
            }
        });

    }

    private void showAlertDialog(String date,View view) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.delete_diary)
                .setMessage(R.string.delete)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEntryService = new EntryService(getContext());
                        mEntryService.deleteDiary(date,getContext());
                        updateView();
                    }
                });
        builder.create().show();
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


    private Boolean CheckDate(int dayOfMonth,int month,int year) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        if (year<y)  return false;
        if (year==y && month<m)  return false;
        if (year==y && month==m && dayOfMonth<=d)  return false;
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateView();
        ButtonAddDay();
    }

    public void onPause() {
        super.onPause();
        ButtonAddDay1();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}