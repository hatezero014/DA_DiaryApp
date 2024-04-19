package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.ui.collection.CarouselModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryPhotoService extends BaseService {
    public EntryPhotoService(Context context) {
        super(context);
    }

    public ArrayList<CarouselModel> getPhotoFromDatabase() {
        db = this.getReadableDatabase();
        ArrayList<CarouselModel> list = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPhoto", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int photoScoreIndex = cursor.getColumnIndex("Photo");
                do {
                    String photo = cursor.getString(photoScoreIndex);
                    list.add(new CarouselModel("/data/user/0/com.example.doan_diaryapp/files/"+photo));
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return list;
    }
}
