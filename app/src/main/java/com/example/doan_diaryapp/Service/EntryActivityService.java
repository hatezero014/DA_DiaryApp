package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryActivity;

import java.util.ArrayList;
import java.util.List;

public class EntryActivityService extends BaseService{
    public EntryActivityService(Context context) {
        super(context);
    }

    public List<Activity> getActivityIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Activity> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryActivity inner join Entry on EntryActivity.EntryId = Entry.Id inner join Activity on EntryActivity.ActivityId = Activity.Id", null)) {
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
                        entryList.add(new Activity(icon));
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

    public List<Activity> getActivityIdByYear(int year){
        db = this.getReadableDatabase();
        List<Activity> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryActivity inner join Entry on EntryActivity.EntryId = Entry.Id inner join Activity on EntryActivity.ActivityId = Activity.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    if(y == year){
                        entryList.add(new Activity(icon));
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
