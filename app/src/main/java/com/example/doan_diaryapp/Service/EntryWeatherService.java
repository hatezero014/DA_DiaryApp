package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.EntryWeather;
import com.example.doan_diaryapp.Models.Weather;

import java.util.ArrayList;
import java.util.List;

public class EntryWeatherService extends BaseService {
    public  EntryWeatherService(Context context) {
        super(context);
    }

    public EntryWeather FindByEntryIdAndWeatherId(Class<EntryWeather> clazz, int entryId, int weatherId) {
        db = this.getReadableDatabase();
        String selection = "EntryId=? AND WeatherId=?";
        String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(weatherId)};
        Cursor cursor = db.query(clazz.getSimpleName(), null, selection, selectionArgs, null, null, null);
        EntryWeather object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        return object;
    }
    public List<Weather> getWeatherIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Weather> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryWeather inner join Entry on EntryWeather.EntryId = Entry.Id inner join Weather on EntryWeather.WeatherId = Weather.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("Desc");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");
                int isActiveColumnIndex = cursor.getColumnIndex("IsActive");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    int isActive = cursor.getInt(isActiveColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Weather(icon, descEn, descVi, isActive));
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
                int descEnColumnIndex = cursor.getColumnIndex("Desc");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");
                int isActiveColumnIndex = cursor.getColumnIndex("IsActive");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    int isActive = cursor.getInt(isActiveColumnIndex);
                    if(y == year){
                        entryList.add(new Weather(icon, descEn, descVi, isActive));
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

    public List<Weather> getWeatherIdByDayMonthYear(int day, int month, int year){
        db = this.getReadableDatabase();
        List<Weather> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryWeather inner join Entry on EntryWeather.EntryId = Entry.Id inner join Weather on EntryWeather.WeatherId = Weather.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("Desc");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");
                int isActiveColumnIndex = cursor.getColumnIndex("IsActive");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int d = Integer.parseInt(parts[3]);
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    int isActive = cursor.getInt(isActiveColumnIndex);
                    if(d == day && m == month && y == year){
                        entryList.add(new Weather(icon, descEn, descVi, isActive));
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

    public List<Weather> getWeatherIdCustom(int byear, int bmonth, int ayear, int amonth){
        db = this.getReadableDatabase();
        List<Weather> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryWeather inner join Entry on EntryWeather.EntryId = Entry.Id inner join Weather on EntryWeather.WeatherId = Weather.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("Desc");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");
                int isActiveColumnIndex = cursor.getColumnIndex("IsActive");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    int isActive = cursor.getInt(isActiveColumnIndex);

                    boolean isAfterLower = (y > byear) || (y == byear && m >= bmonth);

                    boolean isBeforeUpper = (y < ayear) || (y == ayear && m <= amonth);

                    if(isBeforeUpper && isAfterLower){
                        entryList.add(new Weather(icon, descEn, descVi, isActive));
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
