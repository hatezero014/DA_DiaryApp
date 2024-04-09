package com.example.doan_diaryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import android.widget.Toast;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TabLayout DayMonthTab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        DayMonthTab = view.findViewById(R.id.TabBar);
        DayMonthTab.addOnTabSelectedListener(TabBarSelectedListener);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    TabLayout.OnTabSelectedListener TabBarSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    getChildFragmentManager().beginTransaction().replace(R.id.dayandmonth, new DayFragment()).commit();
                    break;
                case 1:
                    getChildFragmentManager().beginTransaction().replace(R.id.dayandmonth, new MonthFragment()).commit();
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            return;
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            return;
        }
    };
}