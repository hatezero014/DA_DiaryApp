package com.example.doan_diaryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.core.app.NotificationManagerCompat;

public class NotificationCancelReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
    private static final int NOTIFICATION_ID = NotificationHelper.NOTIFICATION_ID;
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("CANCEL_NOTIFICATION".equals(intent.getAction())) {
            // Cancel the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(NOTIFICATION_ID);

            // Stop the alarm sound
            stopAlarmSound();
        }
        
        
    }

    private void stopAlarmSound() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
