package com.example.doan_diaryapp.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TabLayout DayMonthTab;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DayMonthTab = view.findViewById(R.id.TabBar);
        viewPager = view.findViewById(R.id.ViewPager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        ConstraintLayout parentLayout = view.findViewById(R.id.parentLayout);
        Drawable background = parentLayout.getBackground();
        int backgroundColor = Color.TRANSPARENT;
        if (background instanceof ColorDrawable) {
            backgroundColor = ((ColorDrawable) background).getColor();
        }
        DayMonthTab.setBackgroundColor(backgroundColor);



        new TabLayoutMediator(DayMonthTab, viewPager, (tab, i) -> {
            if (i == 0)
                tab.setText(getString(R.string.day_view));
            else if (i == 1)
                tab.setText(getString(R.string.month_view));
        }).attach();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}