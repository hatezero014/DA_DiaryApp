package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.doan_diaryapp.Models.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryService extends BaseService{
    public EntryService(Context context) {
        super(context);
    }

    public Entry FindByDate(Entry clazz, String date) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("Entry", null, "Date=?", new String[]{date}, null, null, null);
        Entry object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }

    public List<Entry> getEntriesFromDatabase() {
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date DESC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("Id");
                int noteColumnIndex = cursor.getColumnIndex("Note");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    int id = cursor.getInt(idColumnIndex);
                    String note = cursor.getString(noteColumnIndex).trim();
                    String date = cursor.getString(dateColumnIndex);
                    int d = note.length();
                    if (d != 0) {
                        entryList.add(new Entry(id, note, date));
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

    public int getEntriesNoteFromDatabase(int day, int month, int year, StringBuilder note, AtomicInteger rate) {
        db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int noteColumnIndex = cursor.getColumnIndex("Note");
                int overallScoreIndex = cursor.getColumnIndex("OverallScore");
                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int d = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    if (d == day && month + 1 == m && year == y) {
                        note.setLength(0);
                        note.append(cursor.getString(noteColumnIndex).trim());
                        rate.set(cursor.getInt(overallScoreIndex));
                        return 1;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }

    public List<Entry>getOverallScoreByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date ASC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int m = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    int score = cursor.getInt(scoreColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Entry(score,date));
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

    public List<Entry>getOverallScoreByYear(int year){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date ASC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int y = Integer.parseInt(parts[2]);
                    int score = cursor.getInt(scoreColumnIndex);
                    if(y == year){
                        entryList.add(new Entry(score,date));
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
