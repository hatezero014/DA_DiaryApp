package com.example.doan_diaryapp.Service;
import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Notification;

public class NotificationService extends BaseService{
    public NotificationService(Context context) {super(context);;}

    public Notification FindByDate(Notification clazz, String date) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("Notification", null, "Date=?", new String[]{date}, null, null, null);
        Notification object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }
}
