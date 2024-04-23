package com.example.doan_diaryapp.ui.analyze;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;


import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentAnalyzeBinding;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnalyzeFragment extends Fragment {

    private FragmentAnalyzeBinding binding;

    private TabLayout mTablayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analyze,container,false);

        mTablayout = view.findViewById(R.id.tb_thongkefm);
        viewPager = view.findViewById(R.id.vp_thongkefm);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(mTablayout, viewPager, (tab, i) -> {
            if (i == 0)
                tab.setText(getString(R.string.by_month));
            else if (i == 1)
                tab.setText(getString(R.string.entire_year));
        }).attach();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}