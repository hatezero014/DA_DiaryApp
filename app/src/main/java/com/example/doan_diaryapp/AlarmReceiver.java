package com.example.doan_diaryapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import androidx.appcompat.app.AppCompatActivity;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.doan_diaryapp.NOTIFICATION_PREFS", Context.MODE_PRIVATE);
        boolean isNotificationSwitchChecked = sharedPreferences.getBoolean("notification_switch_state", false);
        if (isNotificationSwitchChecked) {

            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.id_AlarmReceiver))
                    .setSmallIcon(R.drawable.icon_noti)
                    .setContentTitle(context.getString(R.string.add_pocket_diary))
                    .setContentText(context.getString(R.string.add_pocket_diary_description))
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(123, builder.build());
            } else {

            }
        }
    }
}
