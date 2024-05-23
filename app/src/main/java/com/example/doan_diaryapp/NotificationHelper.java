package com.example.doan_diaryapp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "your_channel_id";
    private static final String CHANNEL_NAME = "Your Channel Name";
    private static final String CHANNEL_DESCRIPTION = "Your Channel Description";
    public static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNotificationChannel(Context context, String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

//    public static void showNotification(Context context, String title, String content) {
//        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.icon_noti)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true);
//
//            Intent intent = new Intent(context, ChangeReminderTime.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//            builder.setContentIntent(pendingIntent);
//
//            Intent intent1 = new Intent(context, ChangeReminderTime.class);
//            intent1.setAction("android.intent.action.VIEW"); // Thêm action để xác định khi người dùng nhấn vào thông báo
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//            builder.setContentIntent(pendingIntent1);
//
//
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            try {
//                notificationManager.notify(NOTIFICATION_ID, builder.build());
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Intent intent = new Intent();
//            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//
//            intent.putExtra("app_package", context.getPackageName());
//            intent.putExtra("app_uid", context.getApplicationInfo().uid);
//
//            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
//
//            context.startActivity(intent);
//        }
//    }


}
