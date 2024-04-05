package com.example.doan_diaryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import android.media.MediaPlayer;
import java.util.Calendar;
public class ChangeReminderTime extends BaseActivity {
    private TextView textView;
    private Button button;

    private int selectedHour;
    private int selectedMinute;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_reminder_time);
        textView = findViewById(R.id.timeisSet);
        button = findViewById(R.id.btnShowDiaLog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDiaLog();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                .setTitleText("Select Appointment time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHour = picker.getHour();
                selectedMinute = picker.getMinute();

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
        textView.setText(selectedTime);
    }

    private void startAlarmCheck() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                if (selectedHour == currentHour && selectedMinute == currentMinute) {
                    playAlarmSound();
                }

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    private void stopAlarmCheck() {
        handler.removeCallbacks(runnable);
    }
}