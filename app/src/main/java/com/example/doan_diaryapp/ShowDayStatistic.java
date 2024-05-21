package com.example.doan_diaryapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.DayStatisticAdapter;
import com.example.doan_diaryapp.Models.Statistic;

import java.util.ArrayList;
import java.util.List;

public class ShowDayStatistic extends BaseActivity {
    private RecyclerView recyclerView;
    private DayStatisticAdapter dayStatisticAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_day_statistic);

        recyclerView = findViewById(R.id.rcv_thong_ke_ngay);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null){
            return;
        }
        String date = (String) bundle.get("date");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(this.getResources().getString(R.string.statistics_for)+" "+date);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dayStatisticAdapter = new DayStatisticAdapter(this,date);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dayStatisticAdapter.setData(getListStatistic(date));
        recyclerView.setAdapter(dayStatisticAdapter);
    }

    private List<Statistic> getListStatistic(String date) {
        List<Statistic> list = new ArrayList<>();

        String[]part = date.split("/");
        int month = Integer.parseInt(part[1]);
        int year = Integer.parseInt(part[2]);

        list.add(new Statistic(year,month,1,null));
        list.add(new Statistic(year,month,2,"Mood"));
        list.add(new Statistic(year,month,2,"Activity"));
        list.add(new Statistic(year,month,2,"Partner"));
        list.add(new Statistic(year,month,2,"Weather"));

        return list;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(fragmentManager.getBackStackEntryCount()>0){
                fragmentManager.popBackStack();
            }
            else{
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}