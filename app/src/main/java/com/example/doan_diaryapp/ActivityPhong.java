package com.example.doan_diaryapp;

import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_diaryapp.Adapter.CirclesAdapter;
import com.example.doan_diaryapp.Adapter.ViewPagerAdapter;
import com.example.doan_diaryapp.Models.Circle;
import com.example.doan_diaryapp.Models.Circles;
import com.example.doan_diaryapp.ui.Statistic.ByMonthFragment;
import com.example.doan_diaryapp.ui.Statistic.EntireYearFragment;
import com.google.android.material.tabs.TabLayout;


public class ActivityPhong extends BaseActivity {

    private TabLayout mTablayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);

//        mTablayout = findViewById(R.id.tb_thongke);
//        viewPager = findViewById(R.id.vp_thongke);
//
//        mTablayout.setupWithViewPager(viewPager);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        viewPagerAdapter.addFragment(new ByMonthFragment(), getResources().getString(R.string.by_month_fragment));
//        viewPagerAdapter.addFragment(new EntireYearFragment(), getResources().getString(R.string.entire_year_fragment));
//        viewPager.setAdapter(viewPagerAdapter);

    }
}