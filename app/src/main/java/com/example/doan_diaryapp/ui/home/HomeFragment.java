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

import android.widget.Toast;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    int d=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        MaterialButtonToggleGroup DayMonth = view.findViewById(R.id.toggleGroup);

        if (d == 0) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.dayandmonth, new DayFragment()).commit();
            d=1;
        }

        Button buttonFragmentDay = view.findViewById(R.id.ButtonDay);
        buttonFragmentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMonth.check(R.id.ButtonDay);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, new DayFragment()).commit();
            }
        });

        Button buttonFragmentMonth = view.findViewById(R.id.ButtonMonth);
        buttonFragmentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMonth.check(R.id.ButtonMonth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, new MonthFragment()).commit();
            }
        });

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}


