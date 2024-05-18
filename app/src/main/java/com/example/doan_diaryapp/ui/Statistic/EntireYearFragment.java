package com.example.doan_diaryapp.ui.Statistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.doan_diaryapp.Adapter.YearStatisticAdapter;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EntireYearFragment extends Fragment {

    private RecyclerView recyclerView_year;
    private YearStatisticAdapter yearStatisticAdapter;
    private Spinner spn_yeary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entire_year, container, false);

        spn_yeary = view.findViewById(R.id.spn_yeary);
        updateSpinnerYear(view);

        recyclerView_year = view.findViewById(R.id.rcv_thong_ke_nam);
        yearStatisticAdapter = new YearStatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_year.setLayoutManager(linearLayoutManager);
        yearStatisticAdapter.setData(getListStatistic());
        recyclerView_year.setAdapter(yearStatisticAdapter);
        return view;
    }

    private void updateSpinnerYear(View container) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();

        for(int i = 1990; i<=currentYear;i++){
            years.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(container.getContext(), android.R.layout.simple_spinner_item,years);

        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_yeary.setAdapter(adapterYear);
        spn_yeary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearStatisticAdapter.setData(getListStatistic());
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
        spn_yeary.setSelection(adapterYear.getPosition(currentYear));
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        int year =(Integer) spn_yeary.getSelectedItem();
        mStatistic.clear();

        mStatistic.add(new Statistic(year,1,null));
        mStatistic.add(new Statistic(year,2,"Mood"));
        mStatistic.add(new Statistic(year,2,"Activity"));
        mStatistic.add(new Statistic(year,2,"Partner"));
        mStatistic.add(new Statistic(year,2,"Weather"));

        return mStatistic;
    }
}