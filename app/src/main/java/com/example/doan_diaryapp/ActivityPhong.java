package com.example.doan_diaryapp;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_diaryapp.ui.analyze.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ActivityPhong extends BaseActivity {

    private TabLayout mTablayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);

//        mTablayout = findViewById(R.id.tb_thongke);
//        viewPager = findViewById(R.id.vp_thongke);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
//        viewPager.setAdapter(viewPagerAdapter);
//
//        new TabLayoutMediator(mTablayout, viewPager, (tab, i) -> {
//            if (i == 0)
//                tab.setText(getString(R.string.by_month));
//            else if (i == 1)
//                tab.setText(getString(R.string.entire_year));
//        }).attach();
    }
}