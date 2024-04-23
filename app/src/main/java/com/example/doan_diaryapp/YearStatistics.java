package com.example.doan_diaryapp;

import android.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_diaryapp.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class YearStatistics extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_statistics);

        mTabLayout = findViewById(R.id.statistic_tbl);
        mViewPager = findViewById(R.id.statistic_vp);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}