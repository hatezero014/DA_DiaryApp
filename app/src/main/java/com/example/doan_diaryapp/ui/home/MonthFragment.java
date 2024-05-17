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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doan_diaryapp.ActivityNam;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.databinding.FragmentMonthBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    Calendar calendar = Calendar.getInstance();
    int year ;
    int month ;
    int dayOfMonth ;
    EntryService entryService;

    private void updateView() {
        View rootView = getView();
        if (rootView != null) {
            setCardViewDate(rootView);
            ButtonAddMonth(rootView);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        entryService=new EntryService(getContext());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        setCardViewDate(view);
        ButtonAddMonth(view);

        return view;
    }


    private void setCardViewDate(View view)
    {
        LinearLayout linearLayout=view.findViewById(R.id.show_date_note);
        TextView textViewNote=view.findViewById(R.id.CardViewNote);
        TextView textViewRate=view.findViewById(R.id.CardViewRate);
        MaterialDivider divider = view.findViewById(R.id.divider);
        //ShowNote(view,dayOfMonth,month,year,textViewNote,textViewRate,divider,linearLayout);

        TextView selectedDateTextView = view.findViewById(R.id.CardViewDate);
        //ShowDate(view,dayOfMonth,month+1,year,selectedDateTextView);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int d) {
                dayOfMonth=d; month=m; year=y;
                //ShowNote(view,d,m,y,textViewNote,textViewRate,divider,linearLayout);
                //ShowDate(view,d,m+1,y,selectedDateTextView);
            }
        });
    }

    private void ShowDate(View view,int d,int m,int y, TextView textViewDate)
    {
        String date = d + "-" + m + "-" + y;
        String fullDate = getDayOfWeek(date)+", "+date;
        textViewDate.setText(fullDate);
    }


    private static Language currentLanguage;

    public static void setCurrentLanguage(Language language) {
        currentLanguage = language;
    }
    private String getDayOfWeek(String date) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale locale;
        if (currentLanguage != null && currentLanguage.getCode() != null) {
            locale = new Locale(currentLanguage.getCode());
        } else {
            locale = Locale.getDefault();
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", locale);
        return dayFormat.format(dateTime);
    }


    private void ShowNote(View view,int d,int m,int y,
                          TextView textViewNote,TextView textViewRate, MaterialDivider divider,
                          LinearLayout linearLayout)
    {
        StringBuilder note = new StringBuilder();
        AtomicInteger rate = new AtomicInteger();
        int check = entryService.getEntriesNoteFromDatabase(d,m,y,note,rate);
        textViewNote.setText(note);
        textViewRate.setText("Mood rating: "+rate);


        if (note.length()==0){
            textViewNote.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
        else {
            textViewNote.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }

        if (check==0) {
            linearLayout.setVisibility(View.GONE);
        }
        else  {
            linearLayout.setVisibility(View.VISIBLE);
        }
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