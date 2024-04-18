package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.example.doan_diaryapp.databinding.FragmentDayBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DayFragment extends Fragment {

    private FragmentDayBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        ButtonAddDay(view);

        ListView listView = view.findViewById(R.id.ListDay);
        listView.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1, generateItemList()));

        return view;
    }

    private List<String> generateItemList() {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("Ngày viết nhật kí " + (i + 1) + "\nSự kiện xảy ra " + (i + 1));
        }
        return itemList;
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}