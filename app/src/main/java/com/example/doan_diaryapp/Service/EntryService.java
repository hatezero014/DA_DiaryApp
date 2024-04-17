package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Entry;

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
}
