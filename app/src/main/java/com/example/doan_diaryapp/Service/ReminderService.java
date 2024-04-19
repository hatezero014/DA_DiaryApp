package com.example.doan_diaryapp.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.NotificationHelper;
import com.example.doan_diaryapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderService extends IntentService {
    private static final int NOTIFICATION_ID = 123;

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ImportantDayService importantDayService = new ImportantDayService(this);
            List<ImportantDay> importantDates = importantDayService.GetAll(ImportantDay.class);
            for (ImportantDay date : importantDates) {
                if (shouldRemind(date.getDate())) {
                    sendNotification(date.getDate());
                }
            }
        }
    }

    private boolean shouldRemind(String date) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int importantDay = Integer.parseInt(date.split("-")[0]);
        int importantMonth = Integer.parseInt(date.split("-")[1]);
        int importantYear = Integer.parseInt(date.split("-")[2]);

        return (currentDay == importantDay && currentMonth == importantMonth && currentYear > importantYear);
    }

    private void sendNotification(String date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - Integer.parseInt(date.split("-")[2]);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.icon_noti)
                .setContentTitle(getString(R.string.reminder_title))
                .setContentText(getString(R.string.reminder_message, String.valueOf(year), date))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationHelper.createNotificationChannel(this, "channel_id", "Channel Name");
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

}
