package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryPartner;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.ui.collection.CarouselModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntryPhotoService extends BaseService {
    public EntryPhotoService(Context context) {
        super(context);
    }

    public EntryPhoto FindByEntryIdAndPhotoId(Class<EntryPhoto> clazz, int entryId, int photoId) {
        db = this.getReadableDatabase();
        String selection = "EntryId=? AND PhotoId=?";
        String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(photoId)};
        Cursor cursor = db.query(clazz.getSimpleName(), null, selection, selectionArgs, null, null, null);
        EntryPhoto object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        return object;
    }

    public ArrayList<CarouselModel> getPhotoFromDatabase() {
        db = this.getReadableDatabase();
        ArrayList<CarouselModel> list = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPhoto ORDER BY EntryId DESC", null)) {
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

    public String getDate(String Photo) {
        db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT  * FROM EntryPhoto INNER JOIN Entry ON EntryPhoto.EntryId = Entry.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int photoScoreIndex = cursor.getColumnIndex("Photo");
                int dateScoreIndex = cursor.getColumnIndex("Date");
                do {
                    String date = cursor.getString(dateScoreIndex);
                    String photo = "/data/user/0/com.example.doan_diaryapp/files/"+cursor.getString(photoScoreIndex);
                    if (Photo.equals(photo)) return date;
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return "";
    }


    public List<String> getAllPhoto(String DATE) {
        db = this.getReadableDatabase();
        List<String> entryPhotoList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT  * FROM EntryPhoto INNER JOIN Entry ON EntryPhoto.EntryId = Entry.Id ", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int photoScoreIndex = cursor.getColumnIndex("Photo");
                int dateScoreIndex = cursor.getColumnIndex("Date");
                do {
                    String date = cursor.getString(dateScoreIndex);
                    String photo = cursor.getString(photoScoreIndex);
                    if (DATE.equals(date)) {
                        entryPhotoList.add("/data/user/0/com.example.doan_diaryapp/files/" + photo);
                    }
                    } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryPhotoList;
    }


    public <T> ArrayList<T> GetAllImagesOrderByDESC(Class<T> clazz, String desc) {
        ArrayList<T> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.query(clazz.getSimpleName(), null, null, null, null, null, desc);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                T object = CreateModelObjectFromCursor(clazz, cursor);
                if (object != null) {
                    list.add(object);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public <T> List<T> FindBookmarkByPatternId(Class<T> clazz) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Entry.* FROM EntryPhoto INNER JOIN Entry ON Entry.Id = EntryPhoto.Id ", null);
        List<T> objects = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                T object = CreateModelObjectFromCursor(clazz, cursor);
                objects.add(object);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return objects;
    }
}
