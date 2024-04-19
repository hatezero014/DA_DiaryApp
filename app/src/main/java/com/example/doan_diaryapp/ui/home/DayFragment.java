package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_diaryapp.Adapter.EntryAdapter;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryService;

import com.example.doan_diaryapp.databinding.FragmentDayBinding;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;import java.util.Collections;import java.util.Comparator;
import java.util.SimpleTimeZone;



public class DayFragment extends Fragment {

    private FragmentDayBinding binding;
    private ListView mListView;
    private EntryAdapter mAdapter;
    private EntryService mEntryService;


    private void updateEntries() {
        List<Entry> entryList = mEntryService.getEntriesFromDatabase();
        mAdapter.clear();
        mAdapter.addAll(entryList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        ButtonAddDay(view);
        ListViewDay(view);
        mListView = view.findViewById(R.id.ListDay);
        mEntryService = new EntryService(getContext());
        List<Entry> entryList = mEntryService.getEntriesFromDatabase();
        mAdapter = new EntryAdapter(getContext(), entryList);
        mListView.setAdapter(mAdapter);
        return view;
    }




    private void ListViewDay(View view)
    {
        mListView = view.findViewById(R.id.ListDay);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewDate = view.findViewById(R.id.textViewDate);
                String dateText = textViewDate.getText().toString();
                String[] parts = dateText.split(", ");
                String formattedDate = parts[1].trim();
                String[] dateParts = formattedDate.split("-");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("Date", String.format(Locale.ENGLISH,
                        "%02d-%02d-%04d", day, month, year));
                startActivity(intent);
            }
        });

    }


    private void ButtonAddDay(View view)
    {
        FloatingActionButton buttonAddMonth = view.findViewById(R.id.ButtonAddDay);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        buttonAddMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("Date", String.format(Locale.ENGLISH,
                        "%02d-%02d-%04d", dayOfMonth, month+1, year));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEntries();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}