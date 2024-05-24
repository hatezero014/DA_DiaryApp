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
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.doan_diaryapp.Adapter.YearStatisticAdapter;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryService;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EntireYearFragment extends Fragment {

    private RecyclerView recyclerView_year;
    private YearStatisticAdapter yearStatisticAdapter;
    private AutoCompleteTextView act_yyear;
    private EntryService entryService;
    private TextView tv_statistic_year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entire_year, container, false);

        act_yyear = view.findViewById(R.id.act_yyear);
        updateSpinnerYear(view);

        entryService = new EntryService(getContext());

        tv_statistic_year = view.findViewById(R.id.tv_statistic_year);
        recyclerView_year = view.findViewById(R.id.rcv_thong_ke_nam);
        yearStatisticAdapter = new YearStatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_year.setLayoutManager(linearLayoutManager);
        yearStatisticAdapter.setData(getListStatistic());
        recyclerView_year.setAdapter(yearStatisticAdapter);

        String selectedYear = act_yyear.getText().toString();
        int year = Integer.parseInt(selectedYear);
        List<Entry> entryList = entryService.getOverallScoreByYear(year);

        if(entryList.isEmpty()){
            recyclerView_year.setVisibility(View.GONE);
            tv_statistic_year.setVisibility(View.VISIBLE);
            tv_statistic_year.setText(R.string.no_data_year);
        }
        else {
            recyclerView_year.setVisibility(View.VISIBLE);
            tv_statistic_year.setVisibility(View.GONE);
        }

        return view;
    }

    private void updateSpinnerYear(View container) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();

        for(int i = 2020; i<=currentYear;i++){
            years.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(container.getContext(), R.layout.item_drop_down, years);

        act_yyear.setAdapter(adapterYear);
        act_yyear.setText(String.valueOf(currentYear),false);
        act_yyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearStatisticAdapter.setData(getListStatistic());
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
        act_yyear.setText(String.valueOf(currentYear),false);
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        String selectedYear = act_yyear.getText().toString();
        int year = Integer.parseInt(selectedYear);
        mStatistic.clear();

        mStatistic.add(new Statistic(year,1,null));
        mStatistic.add(new Statistic(year,2,"Mood"));
        mStatistic.add(new Statistic(year,2,"Activity"));
        mStatistic.add(new Statistic(year,2,"Partner"));
        mStatistic.add(new Statistic(year,2,"Weather"));

        return mStatistic;
    }

    @Override
    public void onResume() {
        super.onResume();
        Init();
    }

    public void Init(){
        View view = getView();
        updateSpinnerYear(view);

        yearStatisticAdapter = new YearStatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_year.setLayoutManager(linearLayoutManager);
        yearStatisticAdapter.setData(getListStatistic());
        recyclerView_year.setAdapter(yearStatisticAdapter);

        yearStatisticAdapter.notifyDataSetChanged();

        String selectedYear = act_yyear.getText().toString();
        int year = Integer.parseInt(selectedYear);
        List<Entry> entryList = entryService.getOverallScoreByYear(year);

        if(entryList.isEmpty()){
            recyclerView_year.setVisibility(View.GONE);
            tv_statistic_year.setVisibility(View.VISIBLE);
            tv_statistic_year.setText(R.string.no_data_year);
        }
        else {
            recyclerView_year.setVisibility(View.VISIBLE);
            tv_statistic_year.setVisibility(View.GONE);
        }
    }
}