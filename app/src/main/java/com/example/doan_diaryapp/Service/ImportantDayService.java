package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.ImportantDay;

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
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry INNER JOIN ImportantDay ON Entry.Date = ImportantDay.Date", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("Id");
                int noteColumnIndex = cursor.getColumnIndex("Note");
                int dateColumnIndex = cursor.getColumnIndex("Date");
                do {
                    int id = cursor.getInt(idColumnIndex);
                    String note = cursor.getString(noteColumnIndex).trim();
                    String date = cursor.getString(dateColumnIndex);

                    int d = note.length();
                    if (d != 0 ) {
                        entryList.add(new Entry(id, note, date));
                    }
                } while (cursor.moveToNext());
            }
        }
        finally {
            if (db != null) {
                db.close(); // Đảm bảo đóng kết nối đến cơ sở dữ liệu
            }
        }
        return entryList;
    }
}
