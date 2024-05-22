package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.doan_diaryapp.MainActivity;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryService;

import com.example.doan_diaryapp.databinding.FragmentDayBinding;
import com.example.doan_diaryapp.databinding.FragmentMonthBinding;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;import java.util.Collections;import java.util.Comparator;
import java.util.SimpleTimeZone;



public class
DayFragment extends Fragment {

    private FragmentDayBinding binding;
    private ListView mListView;
    private EntryAdapter mAdapter;
    private EntryService mEntryService;


    private void updateEntries() {
        List<Entry> entryList = mEntryService.getEntriesFromDatabase();
        mAdapter.clear();
        mAdapter.addAll(entryList);
        mAdapter.notifyDataSetChanged();
        TextView textView = getView().findViewById(R.id.text1);
        if (entryList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        ListViewDay(view);

        mListView = view.findViewById(R.id.ListDay);
        mEntryService = new EntryService(getContext());
        List<Entry> entryList = mEntryService.getEntriesFromDatabase();
        mAdapter = new EntryAdapter(getContext(), entryList);
        mListView.setAdapter(mAdapter);

        TextView textView = view.findViewById(R.id.text1);
        if (entryList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
        return view;
    }




    private void ListViewDay(View view)
    {
        mListView = view.findViewById(R.id.ListDay);
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
                        updateEntries();
                    }
                });
        builder.create().show();
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