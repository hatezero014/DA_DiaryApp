package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Partner;

public class PartnerService extends BaseService {
    public PartnerService(Context context) {
        super(context);
    }

    public Partner GetByIcon(Partner clazz, String icon) {
        db = this.getReadableDatabase();
        Partner object = null;
        Cursor cursor = db.query("Partner", null, "Icon=?", new String[]{icon}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }
}
