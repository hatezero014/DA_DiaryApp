package com.example.doan_diaryapp.ui.Statistic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ByMonthFragment extends Fragment {

    private RecyclerView recyclerView_month;
    private StatisticAdapter statisticAdapter;

    public ByMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_by_month, container, false); //pass the correct layout name for the fragment

        recyclerView_month = view.findViewById(R.id.rcv_thong_ke_thang);

        statisticAdapter = new StatisticAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_month.setLayoutManager(linearLayoutManager);
        statisticAdapter.setData(getListStatistic());
        recyclerView_month.setAdapter(statisticAdapter);
        return view;
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        mStatistic.add(new Statistic(2024,2,1));
        mStatistic.add(new Statistic(2024,2,2));
        return mStatistic;
    }


}