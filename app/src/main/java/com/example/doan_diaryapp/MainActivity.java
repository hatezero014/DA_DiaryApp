package com.example.doan_diaryapp;

import android.os.Bundle;
import android.widget.Button;

import com.example.doan_diaryapp.ui.home.DayFragment;
import com.example.doan_diaryapp.ui.home.MonthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.doan_diaryapp.databinding.ActivityMainBinding;
import android.view.View;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    Fragment fragmentDay;
    Fragment fragmentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_collection, R.id.navigation_analyze)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        // code Home
        fragmentDay = new DayFragment();
        fragmentMonth = new MonthFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dayandmonth, fragmentDay).commit();


        Button buttonFragmentDay = findViewById(R.id.ButtonDay);
        Button buttonFragmentMonth = findViewById(R.id.ButtonMonth);

        buttonFragmentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, fragmentDay).commit();
            }
        });

        buttonFragmentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, fragmentMonth).commit();
            }
        });

    }
}