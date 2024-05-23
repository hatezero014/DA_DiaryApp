//package com.example.doan_diaryapp;
//
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.os.Handler;
//import android.os.Looper;
//import androidx.activity.EdgeToEdge;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.FragmentManager;
//
//import com.google.android.material.timepicker.MaterialTimePicker;
//import com.google.android.material.timepicker.TimeFormat;
//import android.media.MediaPlayer;
//import android.widget.Toast;
//
//import java.util.Calendar;
//import java.util.Locale;
//
//public class ChangeReminderTime extends BaseActivity {
//    private SharedPreferences sharedPreferences1;
//    private Calendar calendar;
//    private AlarmManager alarmManager;
//    private PendingIntent pendingIntent;
//    private int selectedHour;
//    private int selectedMinute;
//    TextView textView;
//    Button button;
//    private final ActivityResultLauncher<Intent> requestExactAlarmPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    if (alarmManager.canScheduleExactAlarms()) {
//                        setAlarm();
//                    } else {
//                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_change_reminder_time);
//        createNotificationChannel();
//        button = findViewById(R.id.btnShowDiaLog);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showTimePicker();
//            }
//        });
//        textView = findViewById(R.id.timeisSet);
//
//        sharedPreferences1 = getSharedPreferences("currrentTime", MODE_PRIVATE);
//
//        if (sharedPreferences1.contains("hour") && sharedPreferences1.contains("minute")) {
//            selectedHour = sharedPreferences1.getInt("hour", 0);
//            selectedMinute = sharedPreferences1.getInt("minute", 0);
//
////            startAlarmCheck();
//        }
//
//
//
//
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    private void checkAndRequestExactAlarmPermission() {
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if (alarmManager.canScheduleExactAlarms()) {
//                setAlarm();
//            } else {
//                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                requestExactAlarmPermissionLauncher.launch(intent);
//            }
//        } else {
//            setAlarm();
//        }
//    }
//
//    private void setAlarm() {
//        Intent intent = new Intent(this, NotificationReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        if (calendar != null) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void cancelAlarm() {
//        if (pendingIntent != null) {
//            alarmManager.cancel(pendingIntent);
//            pendingIntent.cancel();
//            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void showTimePicker() {
//        cancelAlarm();
//        calendar = Calendar.getInstance();
//        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
//        int currentMinute = calendar.get(Calendar.MINUTE);
//        int futureHour = currentHour;
//        int futureMinute = currentMinute + 2;
//        if(futureMinute>59){
//            futureMinute = futureMinute-60;
//            futureHour++;
//            if(futureHour>23){
//                futureHour=0;
//            }
//        }
//        SharedPreferences.Editor editor = sharedPreferences1.edit();
//        editor.putInt("hour", currentHour);
//        editor.putInt("minute", currentMinute);
//        editor.apply();
//        calendar.set(Calendar.HOUR_OF_DAY, futureHour);
//        calendar.set(Calendar.MINUTE, futureMinute);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        String period = (futureHour >= 12) ? "PM" : "AM";
//
//        textView.setText(String.format("%02d:%02d %s", currentHour, currentMinute, period));
//        checkAndRequestExactAlarmPermission();
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.remind);
//            String description = getString(R.string.remind_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(getString(R.string.id_NotificationReceiver), name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//
//}