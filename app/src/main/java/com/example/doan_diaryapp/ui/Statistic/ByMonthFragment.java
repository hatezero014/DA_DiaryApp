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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private Spinner spn_yearm;
    private Spinner spn_monthm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_by_month, container, false); //pass the correct layout name for the fragment

        recyclerView_month = view.findViewById(R.id.rcv_thong_ke_thang);

        spn_yearm = view.findViewById(R.id.spn_yearm);
        spn_monthm = view.findViewById(R.id.spn_monthm);

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        statisticAdapter = new StatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_month.setLayoutManager(linearLayoutManager);
        statisticAdapter.setData(getListStatistic());
        recyclerView_month.setAdapter(statisticAdapter);
        return view;
    }

    private void updateSpinnerMonth(View container) {
        ArrayList<Integer> aMonth = new ArrayList<>();
        for(int i=1;i<=12;i++){
            aMonth.add(i);
        }

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(container.getContext(), android.R.layout.simple_spinner_item,aMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_monthm.setAdapter(adapterMonth);
        spn_monthm.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        spn_monthm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statisticAdapter.setData(getListStatistic());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateSpinnerYear(View container) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();

        for(int i = 2023; i <=currentYear;i++){
            years.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(container.getContext(), android.R.layout.simple_spinner_item,years);

        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_yearm.setAdapter(adapterYear);
        spn_yearm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statisticAdapter.setData(getListStatistic());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setDefaultYear(adapterYear);
    }

    private void setDefaultYear(ArrayAdapter<Integer> adapterYear) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean yearExists = false;
        for (int i = 0; i < adapterYear.getCount(); i++) {
            if (adapterYear.getItem(i) == currentYear) {
                yearExists = true;
                break;
            }
        }
        if (!yearExists) {
            adapterYear.add(currentYear);
            adapterYear.notifyDataSetChanged();
        }
        spn_yearm.setSelection(adapterYear.getPosition(currentYear));
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        int month = (Integer)spn_monthm.getSelectedItem();
        int year = (Integer)spn_yearm.getSelectedItem();
        mStatistic.clear();
        mStatistic.add(new Statistic(year,month,1,null));
        mStatistic.add(new Statistic(year,month,2,"Mood"));
        mStatistic.add(new Statistic(year,month,2,"Activity"));
        mStatistic.add(new Statistic(year,month,2,"Partner"));
        mStatistic.add(new Statistic(year,month,2,"Weather"));

        return mStatistic;
    }


}