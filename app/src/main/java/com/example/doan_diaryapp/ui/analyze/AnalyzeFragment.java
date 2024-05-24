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
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.NotificationService;
import com.example.doan_diaryapp.databinding.FragmentAnalyzeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class AnalyzeFragment extends Fragment {

    private FragmentAnalyzeBinding binding;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    TextView tv_count_notification;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AnalyzeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AnalyzeViewModel.class);

        binding = FragmentAnalyzeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tv_count_notification = root.findViewById(R.id.count_notification2);

        viewPager2 = root.findViewById(R.id.vp_thongkefm);
        tabLayout = root.findViewById(R.id.tab_thongke);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, i) -> {
            if(i==0)
                tab.setText(R.string.by_month);
            else if(i == 1)
                tab.setText(R.string.entire_year); 
            else tab.setText(R.string.custom);
        }).attach();
        CountNotification();
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        CountNotification();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void CountNotification(){
        List<Notification> list = getListNotification();
        int countNotification = list.size();
        int length = String.valueOf(countNotification).length();
        if(countNotification==0 || list == null){
            tv_count_notification.setVisibility(View.GONE);
        }
        else if(length == 1){
            tv_count_notification.setPadding(20,3,20,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText(String.valueOf(countNotification));
        }
        else if(length == 2){
            tv_count_notification.setPadding(13,3,13,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText(String.valueOf(countNotification));
        }
        else if(length > 2){
            tv_count_notification.setPadding(6,3,6,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText("99+");
        }

    }

    public List<Notification> getListNotification(){
        NotificationService notificationService = new NotificationService(getContext());
        List<Notification> list = notificationService.GetcountNotificationisnotRead(Notification.class);
        return list;

    }
}