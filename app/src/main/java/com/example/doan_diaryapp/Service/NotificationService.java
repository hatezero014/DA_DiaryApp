package com.example.doan_diaryapp.Service;
import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends BaseService {
    public NotificationService(Context context) {
        super(context);
        ;
    }

    public <T> ArrayList<T> GetAllOrderByDESC(Class<T> clazz, String desc, String whereClause, String[] whereArgs) {
        ArrayList<T> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.query(clazz.getSimpleName(), null, whereClause, whereArgs, null, null, desc);
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

    public <T> List<T> DayDistinct(Class<T> clazz) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT day from Notification order by Id DESC", null);
        List<T> objects = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                T object = CreateModelObjectFromCursor(clazz, cursor);
                objects.add(object);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return objects;
    }
}

