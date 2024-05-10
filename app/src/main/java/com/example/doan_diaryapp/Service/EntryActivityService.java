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
    public EntryActivity FindByEntryIdAndActivityId(Class<EntryActivity> clazz, int entryId, int activityId) {
        db = this.getReadableDatabase();
        String selection = "EntryId=? AND ActivityId=?";
        String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(activityId)};
        Cursor cursor = db.query(clazz.getSimpleName(), null, selection, selectionArgs, null, null, null);
        EntryActivity object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        return object;
    }

    public List<Activity> getActivityIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Activity> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryActivity inner join Entry on EntryActivity.EntryId = Entry.Id inner join Activity on EntryActivity.ActivityId = Activity.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int m = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Activity(icon, descEn, descVi));
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
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    if(y == year){
                        entryList.add(new Activity(icon, descEn, descVi));
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
