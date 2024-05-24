package com.example.doan_diaryapp.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.NotificationService;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TabLayout DayMonthTab;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TextView tv_count_notification;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DayMonthTab = view.findViewById(R.id.TabBar);
        viewPager = view.findViewById(R.id.ViewPager);
        tv_count_notification = view.findViewById(R.id.count_notification1);

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
        CountNotification();

        return view;
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