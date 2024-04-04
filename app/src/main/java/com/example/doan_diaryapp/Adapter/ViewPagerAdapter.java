package com.example.doan_diaryapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_diaryapp.ui.Statistic.ByMonthFragment;
import com.example.doan_diaryapp.ui.Statistic.EntireYearFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EntireYearFragment();
            case 1:
                return  new ByMonthFragment();
            default:
                return new EntireYearFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Entire Year";
                break;
            case 1:
                title = "By Month";
                break;
        }
        return title;
    }
}
