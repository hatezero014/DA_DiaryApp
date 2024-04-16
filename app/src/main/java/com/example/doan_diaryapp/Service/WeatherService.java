package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Weather;

public class WeatherService extends BaseService {
    public  WeatherService(Context context) {
        super(context);
    }

    public Weather GetByIcon(Weather clazz, String icon) {
        db = this.getReadableDatabase();
        Weather object = null;
        Cursor cursor = db.query("Weather", null, "Icon=?", new String[]{icon}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }
}
