package com.example.doan_diaryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.Service.LanguageService;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SettingActivity extends BaseActivity {

    Dialog dialog;
    TextView textViewSubTheme, textViewSubLanguage, textViewNotificationAlarm;
    Switch switchNotification, switchSecurity;
    LinearLayout layoutSecurity ,changePasswordButton, deletePasswordButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences1;
    private int selectedHour;
    private int selectedMinute;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    MaterialDivider div1, div2;

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
        // Initially hide ChangePassword and DeletePassword buttons
        layoutSecurity.setVisibility(View.VISIBLE);
        changePasswordButton.setVisibility(View.GONE);
        deletePasswordButton.setVisibility(View.GONE);
        div1.setVisibility(View.GONE);
        div2.setVisibility(View.GONE);


        NotificationHelper.createNotificationChannel(this);

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW")) {
            // Xử lý khi người dùng nhấn vào thông báo
            stopAlarmCheck(); // Tắt kiểm tra báo thức
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NotificationHelper.NOTIFICATION_ID); // Xóa thông báo
        }

        if (sharedPreferences1.contains("hour") && sharedPreferences1.contains("minute")) {
            // Nếu có, đặt thời gian báo thức tương ứng
            selectedHour = sharedPreferences1.getInt("hour", 0);
            selectedMinute = sharedPreferences1.getInt("minute", 0);
            handleSelectedTime(selectedHour, selectedMinute);
            startAlarmCheck();
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

                if (isChecked) { // Nếu switch đang ở trạng thái false
                    openDiaLog(); // Mở dialog
                    switchNotification.setChecked(true); // Đặt switch thành true
                    editor.putBoolean("notification_switch_state", true); // Lưu trạng thái vào SharedPreferences
                } else { // Nếu switch đang ở trạng thái true
                    textViewNotificationAlarm.setText(null); // Gán textView thành null
                    switchNotification.setChecked(false); // Đặt switch thành false
                    editor.putBoolean("notification_switch_state", false); // Lưu trạng thái vào SharedPreferences
                }

                editor.apply();

            }
        });
//        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    openDiaLog();
//                    switchNotification.setChecked(true);
//                    SharedPreferences.Editor editor = sharedPreferences1.edit();
//                    editor.putBoolean("notification_switch_state", true);
//                    editor.apply();
//                }
//                else {
//                    textViewNotificationAlarm.setText(null);
//                    SharedPreferences.Editor editor = sharedPreferences1.edit();
//                    editor.putBoolean("notification_switch_state", false);
//                    editor.apply();
//                }
//            }
//        });
    }

    private void updateNotificationSwitchState() {
        boolean isNotificationSwitchChecked = sharedPreferences1.getBoolean("notification_switch_state", false);
        switchNotification.setChecked(isNotificationSwitchChecked);
        if (isNotificationSwitchChecked) {
            int hour = sharedPreferences1.getInt("hour", -1);
            int minute = sharedPreferences1.getInt("minute", -1);
            if (hour != -1 && minute != -1) {
                handleSelectedTime(hour, minute);
            }
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

//    private boolean isReturningFromAnotherActivity = false;
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences.Editor editor = sharedPreferences1.edit();
//        editor.putBoolean("is_returning_from_another_activity", true); // Đặt cờ khi rời khỏi activity
//        editor.apply();
//        isReturningFromAnotherActivity = false; // Reset biến khi rời khỏi activity
//    }

    @Override
    protected void onResume() {
        super.onResume();
        previousSwitchState = switchSecurity.isChecked();
        boolean receivedBoolean = getIntent().getBooleanExtra("myBooleanKey", true);
        String savedPasscode = sharedPreferences.getString("passcode", null);
        if (!isPasscodeVerified && receivedBoolean) {


            if (savedPasscode != null) {
//
//                switchSecurity.setVisibility(View.VISIBLE);
//                changePasswordButton.setVisibility(View.GONE);
//                deletePasswordButton.setVisibility(View.GONE);

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

//                switchSecurity.setVisibility(View.GONE);
//                changePasswordButton.setVisibility(View.VISIBLE);
//                deletePasswordButton.setVisibility(View.VISIBLE);
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
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(currentHour)
                .setMinute(currentMinute)
                .setTitleText(R.string.select_reminder_time)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHour = picker.getHour();
                selectedMinute = picker.getMinute();

                // Lưu thời gian báo thức vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("hour", selectedHour);
                editor.putInt("minute", selectedMinute);
                editor.apply();

                // Gọi startAlarmCheck() sau khi chọn thời gian
                startAlarmCheck();

                handleSelectedTime(selectedHour, selectedMinute);
            }
        });

        picker.show(fragmentManager, "tag");

    }

    private void playAlarmSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        mediaPlayer.start();
    }

    private void handleSelectedTime(int hour, int minute) {
        String selectedTime = String.format("%02d:%02d", hour, minute);
        textViewNotificationAlarm.setText(selectedTime);
    }

    private void startAlarmCheck() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // Lấy ngày hiện tại dưới dạng một chuỗi duy nhất để lưu trữ thông tin của lần cuối cùng mà thông báo đã được gửi
                String currentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

                SharedPreferences sharedPreferences2 = getSharedPreferences("com.example.doan_diaryapp.NOTIFICATION_PREFS", MODE_PRIVATE);
                String lastNotificationDate = sharedPreferences2.getString("last_notification_date", "");

                int savedHour = currentHour;
                int savedMinute = currentMinute;

                // Nếu đã có thời gian báo thức được thiết lập trước đó, sử dụng thời gian đó
                if (sharedPreferences2.contains("hour") && sharedPreferences2.contains("minute")) {
                    savedHour = sharedPreferences2.getInt("hour", currentHour);
                    savedMinute = sharedPreferences2.getInt("minute", currentMinute);
                } else {
                    // Nếu không, không thiết lập bất kỳ báo thức nào
                    return;
                }

                if (selectedHour == currentHour && selectedMinute == currentMinute &&
                        (!currentDate.equals(lastNotificationDate) || (savedHour == currentHour && savedMinute == currentMinute))) {
                    NotificationHelper.showNotification(SettingActivity.this, "Báo thức", "Đã đến giờ báo thức");

                    // Cập nhật thời gian cuối cùng thông báo được gửi là ngày hôm nay
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("last_notification_date", currentDate);
                    editor.apply();
                    handler.removeCallbacks(this);
                    //playAlarmSound(); // Nếu muốn phát âm thanh, bỏ comment dòng này
                }
                else{
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }

    private void stopAlarmCheck() {
        handler.removeCallbacks(runnable);
    }


}