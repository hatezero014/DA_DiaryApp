package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import android.media.MediaPlayer;
import java.util.Calendar;
import java.util.Locale;

public class ChangeReminderTime extends BaseActivity {

    private SharedPreferences sharedPreferences1;

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

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences1 = getSharedPreferences("com.example.doan_diaryapp.NOTIFICATION_PREFS", MODE_PRIVATE);

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
        openDiaLog();


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                openDiaLog();
//            }
//        });
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
                    NotificationHelper.showNotification(ChangeReminderTime.this, "Báo thức", "Đã đến giờ báo thức");

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