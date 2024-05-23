package com.example.doan_diaryapp.ui.Statistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;

import com.example.doan_diaryapp.Adapter.MonthStatisticAdapter;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ByMonthFragment extends Fragment {

    private RecyclerView recyclerView_month;
    private MonthStatisticAdapter monthStatisticAdapter;
    private AutoCompleteTextView act_mmonth;
    private AutoCompleteTextView act_myear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_by_month, container, false); //pass the correct layout name for the fragment

        recyclerView_month = view.findViewById(R.id.rcv_thong_ke_thang);

        act_mmonth = view.findViewById(R.id.act_mmonth);
        act_myear = view.findViewById(R.id.act_myear);

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        monthStatisticAdapter = new MonthStatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_month.setLayoutManager(linearLayoutManager);
        monthStatisticAdapter.setData(getListStatistic());
        recyclerView_month.setAdapter(monthStatisticAdapter);
        return view;
    }

    private void updateSpinnerMonth(View container) {
        ArrayList<Integer> aMonth = new ArrayList<>();
        for(int i=1;i<=12;i++){
            aMonth.add(i);
        }

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(container.getContext(), R.layout.item_drop_down, aMonth);
        act_mmonth.setAdapter(adapterMonth);

        act_mmonth.setText(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1),false);

        act_mmonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthStatisticAdapter.setData(getListStatistic());
            }
        });
    }

    private void updateSpinnerYear(View container) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<Integer> years = new ArrayList<>();

        for(int i = 2000; i <=currentYear;i++){
            years.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(container.getContext(), R.layout.item_drop_down,years);
        act_myear.setAdapter(adapterYear);

        act_myear.setText(String.valueOf(currentYear),false);

        act_myear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthStatisticAdapter.setData(getListStatistic());
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
        act_myear.setText(String.valueOf(currentYear),false);
    }

    private List<Statistic> getListStatistic() {
        List<Statistic> mStatistic = new ArrayList<>();
        String selectedYear = act_myear.getText().toString();
        int year = Integer.parseInt(selectedYear);
        String selectedMonth = act_mmonth.getText().toString();
        int month = Integer.parseInt(selectedMonth);
        mStatistic.clear();
        mStatistic.add(new Statistic(year,month,1,null));
        mStatistic.add(new Statistic(year,month,2,"Mood"));
        mStatistic.add(new Statistic(year,month,2,"Activity"));
        mStatistic.add(new Statistic(year,month,2,"Partner"));
        mStatistic.add(new Statistic(year,month,2,"Weather"));

        return mStatistic;
    }

    @Override
    public void onResume() {
        super.onResume();
        Init();
    }

    public void Init(){
        View view = getView();

        monthStatisticAdapter = new MonthStatisticAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_month.setLayoutManager(linearLayoutManager);
        monthStatisticAdapter.setData(getListStatistic());
        recyclerView_month.setAdapter(monthStatisticAdapter);

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        monthStatisticAdapter.notifyDataSetChanged();
    }
}