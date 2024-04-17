package com.example.doan_diaryapp.ui.Statistic;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan_diaryapp.Adapter.StatisticAdapter;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


public class EntireYearFragment extends Fragment {

    private RecyclerView recyclerView_year;
    private StatisticAdapter statisticAdapter;

    public EntireYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entire_year, container, false);

        statisticAdapter = new StatisticAdapter();

        recyclerView_year = view.findViewById(R.id.rcv_thong_ke_nam);
        statisticAdapter = new StatisticAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_year.setLayoutManager(linearLayoutManager);
        statisticAdapter.setData(getListStatistic());
        recyclerView_year.setAdapter(statisticAdapter);
        return view;
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        mStatistic.add(new Statistic(2024,1));
        mStatistic.add(new Statistic(2024,2));
        return mStatistic;
    }
}