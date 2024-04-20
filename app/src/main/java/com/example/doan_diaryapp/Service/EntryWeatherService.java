package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Weather;

import java.util.ArrayList;
import java.util.List;

public class EntryWeatherService extends BaseService {
    public  EntryWeatherService(Context context) {
        super(context);
    }

    public List<Weather> getWeatherIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Weather> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryWeather inner join Entry on EntryWeather.EntryId = Entry.Id inner join Weather on EntryWeather.WeatherId = Weather.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int m = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Weather(icon));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    public List<Weather> getWeatherIdByYear(int year){
        db = this.getReadableDatabase();
        List<Weather> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryWeather inner join Entry on EntryWeather.EntryId = Entry.Id inner join Weather on EntryWeather.WeatherId = Weather.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    if(y == year){
                        entryList.add(new Weather(icon));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }
}
