package com.example.doan_diaryapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan_diaryapp.ui.home.DayFragment;
import com.example.doan_diaryapp.ui.home.HomeFragment;
import com.example.doan_diaryapp.ui.home.MonthFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new MonthFragment();
        return new DayFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
