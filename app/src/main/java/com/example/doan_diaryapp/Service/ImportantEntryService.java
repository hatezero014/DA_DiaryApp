package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.ImportantEntry;

import java.util.ArrayList;
import java.util.List;

public class ImportantEntryService extends BaseService {
    public ImportantEntryService(Context context) {
        super(context);
    }

    public ImportantEntry FindByDate(ImportantEntry clazz, String date) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("ImportantDay", null, "Date=?", new String[]{date}, null, null, null);
        ImportantEntry object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }



    public List<Entry> getEntriesFromDatabaseQT() {
        db = this.getReadableDatabase();
        String DATE = "";
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry INNER JOIN ImportantEntry ON Entry.Id = ImportantEntry.EntryId ORDER BY SUBSTR(Entry.Date, 16, 4) || SUBSTR(Entry.Date, 13, 2) || SUBSTR(Entry.Date, 10, 2)||SUBSTR(Entry.Date, 1, 2) || '-' || SUBSTR(Entry.Date, 4, 2) || '-' || SUBSTR(Entry.Date, 7, 2) DESC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("Id");
                int noteColumnIndex = cursor.getColumnIndex("Note");
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int titleColumnIndex = cursor.getColumnIndex("Title");
                do {
                    int id = cursor.getInt(idColumnIndex);
                    String note = cursor.getString(noteColumnIndex).trim();
                    String title = cursor.getString(titleColumnIndex).trim();
                    String date = cursor.getString(dateColumnIndex);
                    String day=date.substring(date.length() - 10);

                    if (DATE.equals(day)) {
                        entryList.add(new Entry(id, note, date,title));
                    } else {
                        DATE=day;
                        entryList.add(new Entry(id, DATE, "",""));
                        entryList.add(new Entry(id, note, date,title));
                    }

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
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry INNER JOIN ImportantEntry ON Entry.Id = ImportantEntry.EntryId", null)) {
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
