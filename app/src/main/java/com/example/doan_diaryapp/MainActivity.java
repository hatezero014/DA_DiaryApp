package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.doan_diaryapp.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity {
    SharedPreferences sharedPreferences;
    String passCode;


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sharedPreferences = getSharedPreferences("Passcode", MODE_PRIVATE);

        boolean receivedBoolean = getIntent().getBooleanExtra("isCheckMainActivity", false);
        passCode = sharedPreferences.getString("passcode", null);
        if(passCode!=null && !receivedBoolean){
            Intent intent = new Intent(MainActivity.this, OpenPasscodeView.class);
            intent.putExtra("action", "verify");
            startActivity(intent);
            finish();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_collection, R.id.navigation_analyze)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void Setting_onClick(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        intent.putExtra("myBooleanKey", false);
        startActivity(intent);

    }

}