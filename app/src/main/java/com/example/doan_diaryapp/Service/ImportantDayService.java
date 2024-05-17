package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ImportantDayService extends BaseService {
    public ImportantDayService(Context context) {
        super(context);
    }

    public ImportantDay FindByDate(ImportantDay clazz, String date) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("ImportantDay", null, "Date=?", new String[]{date}, null, null, null);
        ImportantDay object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }



    public List<Entry> getEntriesFromDatabaseQT() {
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry INNER JOIN ImportantDay ON Entry.Date = ImportantDay.Date ORDER BY SUBSTR(Entry.Date, 16, 4) || SUBSTR(Entry.Date, 13, 2) || SUBSTR(Entry.Date, 10, 2)||SUBSTR(Entry.Date, 1, 2) || '-' || SUBSTR(Entry.Date, 4, 2) || '-' || SUBSTR(Entry.Date, 7, 2) DESC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("Id");
                int noteColumnIndex = cursor.getColumnIndex("Note");
                int dateColumnIndex = cursor.getColumnIndex("Date");
                do {
                    int id = cursor.getInt(idColumnIndex);
                    String note = cursor.getString(noteColumnIndex).trim();
                    String date = cursor.getString(dateColumnIndex);
                    entryList.add(new Entry(id, note, date));
                } while (cursor.moveToNext());
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    public boolean checkImportant(String checkDate) {
        db = this.getReadableDatabase();
        int d=0;
        try (Cursor cursor = db.rawQuery("SELECT * FROM ImportantDay", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                do {
                    String date = cursor.getString(dateColumnIndex);
                    if (checkDate.equals(date)) return true;
                } while (cursor.moveToNext());
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }



}
