package com.example.doan_diaryapp.ui.analyze;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.doan_diaryapp.ui.Statistic.ByMonthFragment;
import com.example.doan_diaryapp.ui.Statistic.EntireYearFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new ByMonthFragment();
        return new EntireYearFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}