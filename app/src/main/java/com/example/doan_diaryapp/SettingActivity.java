package com.example.doan_diaryapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.Service.LanguageService;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import java.util.Calendar;


public class SettingActivity extends BaseActivity {

    Dialog dialog;
    TextView textViewSubTheme, textViewSubLanguage, textViewNotificationAlarm;
    MaterialSwitch switchNotification, switchSecurity;
    LinearLayout layoutSecurity ,changePasswordButton, deletePasswordButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences1;
    int selectedHour ;
    int selectedMinute;
    MaterialDivider div1, div2;

    private MaterialTimePicker materialTimePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private final ActivityResultLauncher<Intent> requestExactAlarmPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        setAlarm();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void setAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (calendar != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createNotificationChannel();

        textViewSubTheme = findViewById(R.id.textviewSubTheme);
        textViewSubLanguage = findViewById(R.id.textviewSubLanguage);
        switchNotification = findViewById(R.id.switch_notification);
        textViewNotificationAlarm = findViewById(R.id.notification_alarm_clock);
        switchSecurity = findViewById(R.id.switch_security);
        layoutSecurity = findViewById(R.id.layout_Security);
        changePasswordButton = findViewById(R.id.change_PIN);
        deletePasswordButton = findViewById(R.id.delete_PIN);
        div1 = findViewById(R.id.div1);
        div2 = findViewById(R.id.div2);

        sharedPreferences = getSharedPreferences("Passcode", MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("com.example.doan_diaryapp.NOTIFICATION_PREFS", MODE_PRIVATE);

        layoutSecurity.setVisibility(View.VISIBLE);
        changePasswordButton.setVisibility(View.GONE);
        deletePasswordButton.setVisibility(View.GONE);
        div1.setVisibility(View.GONE);
        div2.setVisibility(View.GONE);

        if (sharedPreferences1.contains("hour") && sharedPreferences1.contains("minute")) {
            selectedHour = sharedPreferences1.getInt("hour", 0);
            selectedMinute = sharedPreferences1.getInt("minute", 0);
            handleSelectedTime(selectedHour, selectedMinute);
        }

        initLanguage();
        customDialog();
        setupSwitchNotification();
        setUpSwitchSecurity();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.setting_title));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupSwitchNotification() {
        switchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = switchNotification.isChecked();
                SharedPreferences.Editor editor = sharedPreferences1.edit();

                if (isChecked) {
                    openDiaLog();
                    switchNotification.setChecked(true);
                    editor.putBoolean("notification_switch_state", true);
                } else {
                    textViewNotificationAlarm.setText(null);
                    switchNotification.setChecked(false);
                    editor.putBoolean("notification_switch_state", false);
                    cancelAlarm();
                    editor.remove("hour");
                    editor.remove("minute");
                }

                editor.apply();

            }
        });
    }

    private void cancelAlarm() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void onLayoutThemeClick(View view) {
        dialog.show();
    }

    public void onLayoutLanguageClick(View view) {
        Intent intent = new Intent(this, ChangeLanguage.class);
        startActivity(intent);
        initLanguage();
    }

    void initLanguage() {
        LanguageService languageService = new LanguageService(this);
        Language language = languageService.FindById(Language.class, 1);
        if (language.getIsActive() == 1) {
            textViewSubLanguage.setText(getString(R.string.language_vi));
        }
        else {
            textViewSubLanguage.setText(getString(R.string.language_en));
        }
    }

    public void onLayoutFeedbackClick(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void customDialog() {
        dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.dialog_display_mode);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_display_mode));
        dialog.setCancelable(true);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RadioButton rBtnLight = dialog.findViewById(R.id.rBtnLight);
        RadioButton rBtnDark = dialog.findViewById(R.id.rBtnDark);
        RadioButton rBtnSystem = dialog.findViewById(R.id.rBtnSystem);

        int displayMode = getSharedPreferences("MODE", Context.MODE_PRIVATE).getInt("displayMode", 2);
        if (displayMode == 0) {
            rBtnLight.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_light));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (displayMode == 1){
            rBtnDark.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_dark));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            rBtnSystem.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_system));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        rBtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 0);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_light));
                dialog.dismiss();
            }
        });

        rBtnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 1);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_dark));
                dialog.dismiss();
            }
        });

        rBtnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 2);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_system));
                dialog.dismiss();
            }
        });
    }
    private void setUpSwitchSecurity()
    {
        switchSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent = new Intent(SettingActivity.this, OpenPasscodeView.class);
                    intent.putExtra("action", "create");
                    startActivity(intent);
                    //finish();
                }
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "change");
                startActivity(intent);
                finish();
            }
        });

        deletePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "delete");
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isPasscodeVerified = false;
    private boolean previousSwitchState = false;

    @Override
    protected void onResume() {
        super.onResume();
        previousSwitchState = switchSecurity.isChecked();
        boolean receivedBoolean = getIntent().getBooleanExtra("myBooleanKey", true);
        String savedPasscode = sharedPreferences.getString("passcode", null);
        if (!isPasscodeVerified && receivedBoolean) {


            if (savedPasscode != null) {
                layoutSecurity.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.GONE);
                deletePasswordButton.setVisibility(View.GONE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
                switchSecurity.setChecked(true);


                Intent intent = new Intent(SettingActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "verify");
                startActivity(intent);
                finish();
            }
        }
        else {
            switchSecurity.setChecked(false);

            if(savedPasscode!=null) {
                layoutSecurity.setVisibility(View.GONE);
                changePasswordButton.setVisibility(View.VISIBLE);
                deletePasswordButton.setVisibility(View.VISIBLE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.VISIBLE);
            }
            else {
                layoutSecurity.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.GONE);
                deletePasswordButton.setVisibility(View.GONE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
            }

        }
        isPasscodeVerified = true;
        boolean isNotificationSwitchChecked = sharedPreferences1.getBoolean("notification_switch_state", false);
        switchNotification.setChecked(isNotificationSwitchChecked);


    }

    private void openDiaLog()
    {
        calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(currentHour)
                .setMinute(currentMinute)
                .setTitleText(R.string.select_reminder_time)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("hour", hour);
                editor.putInt("minute", minute);
                editor.apply();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                String period = (hour >= 12) ? "PM" : "AM";
                if (hour > 12) hour -= 12;
                textViewNotificationAlarm.setText(String.format("%02d:%02d %s", hour, minute, period));
                checkAndRequestExactAlarmPermission();
            }

        });
        materialTimePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("notification_switch_state", false);
                editor.apply();
                switchNotification.setChecked(false);
            }
        });

        materialTimePicker.show(fragmentManager, getString(R.string.id_AlarmReceiver));

    }

    private void checkAndRequestExactAlarmPermission() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                setAlarm();
            } else {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                requestExactAlarmPermissionLauncher.launch(intent);
            }
        } else {
            setAlarm();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.alarm_clock);
            String description = getString(R.string.alarm_clock_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.id_AlarmReceiver), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void handleSelectedTime(int hour, int minute) {
        String selectedTime = String.format("%02d:%02d", hour, minute);
        textViewNotificationAlarm.setText(selectedTime);
    }

}