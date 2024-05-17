package com.example.doan_diaryapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan_diaryapp.ui.Statistic.ByMonthFragment;
import com.example.doan_diaryapp.ui.Statistic.CustomFragment;
import com.example.doan_diaryapp.ui.Statistic.EntireYearFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new ByMonthFragment();
        //else if(position == 1)
        return new EntireYearFragment();
        //return new CustomFragment();
    }

    @Override
    public int getItemCount() {
        //return 3;
        return 2;
    }
}