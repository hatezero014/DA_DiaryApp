package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Emotion;

public class ActivityService extends BaseService {
    public ActivityService(Context context) {
        super(context);
    }

    public Activity GetByIcon(Activity clazz, String icon) {
        db = this.getReadableDatabase();
        Activity object = null;
        Cursor cursor = db.query("Activity", null, "Icon=?", new String[]{icon}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }
}
