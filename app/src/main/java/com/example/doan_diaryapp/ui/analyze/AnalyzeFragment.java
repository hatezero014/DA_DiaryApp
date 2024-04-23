package com.example.doan_diaryapp.ui.analyze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_diaryapp.Adapter.ViewPagerAdapter;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentAnalyzeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnalyzeFragment extends Fragment {

    private FragmentAnalyzeBinding binding;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AnalyzeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AnalyzeViewModel.class);

        binding = FragmentAnalyzeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 = root.findViewById(R.id.vp_thongkefm);
        tabLayout = root.findViewById(R.id.tab_thongke);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, i) -> {
            if(i==0)
                tab.setText(R.string.by_month);
            else  tab.setText(R.string.entire_year);
        }).attach();
        
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}