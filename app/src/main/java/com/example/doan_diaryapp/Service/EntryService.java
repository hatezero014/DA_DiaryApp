package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.doan_diaryapp.Models.Entry;

import java.util.ArrayList;
import java.util.List;

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
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry", null)) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryList;
    }



}
