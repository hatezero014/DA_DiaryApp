package com.example.doan_diaryapp.Service;
import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Notification;

import java.util.ArrayList;

public class NotificationService extends BaseService{
    public NotificationService(Context context) {super(context);;}

    public <T> ArrayList<T> GetAllOrderByDESC(Class<T> clazz, String desc) {
        ArrayList<T> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.query(clazz.getSimpleName(), null, null, null, null, null, desc);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                T object = CreateModelObjectFromCursor(clazz, cursor);
                if (object != null) {
                    list.add(object);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
