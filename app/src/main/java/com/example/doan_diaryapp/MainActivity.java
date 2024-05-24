package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.doan_diaryapp.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.doan_diaryapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends BaseActivity {
    SharedPreferences sharedPreferences;
    String passCode;
    FloatingActionButton fab;

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
        if(passCode != null && !receivedBoolean){
            Intent intent = new Intent(MainActivity.this, OpenPasscodeView.class);
            intent.putExtra("action", "verify");
            startActivity(intent);
            finish();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_collection, R.id.navigation_analyze, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("Date", String.format(Locale.ENGLISH,
                        "%02d:%02d:%02d %02d-%02d-%04d", hour,minute,second,dayOfMonth, month+1, year));
                startActivity(intent);
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Bundle bundle = new Bundle();
                if(menuItem.getItemId() == R.id.navigation_setting){
                    bundle.putBoolean("myBooleanKey", false);
                    navController.navigate(R.id.navigation_setting, bundle);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_collection)
                {
                    navController.navigate(R.id.navigation_collection);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_analyze)
                {
                    navController.navigate(R.id.navigation_analyze);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_home)
                {
                    navController.navigate(R.id.navigation_home);
                    return true;
                }
                return false;
            }
        });
    }


    public void Setting_onClick(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        intent.putExtra("myBooleanKey", false);
        startActivity(intent);

    }

    public void Notification_onClick(MenuItem item){
        Intent intent = new Intent(MainActivity.this, NotificationApp.class);
        startActivity(intent);
    }

}