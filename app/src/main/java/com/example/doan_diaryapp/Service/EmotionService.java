package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.EntryEmotion;

public class EmotionService extends BaseService {
    public EmotionService(Context context) {
        super(context);
    }

    public Emotion GetByIcon(Emotion clazz, String icon) {
        db = this.getReadableDatabase();
        Emotion object = null;
        Cursor cursor = db.query("Emotion", null, "Icon=?", new String[]{icon}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz.getClass(), cursor);
            cursor.close();
        }
        return object;
    }
}
