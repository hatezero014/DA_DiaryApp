package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.ImportantDay;

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
}
