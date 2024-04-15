package com.example.doan_diaryapp.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


import java.lang.reflect.Field;

public class BaseService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diary.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public BaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        copyDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Không cần thực hiện bất kỳ hành động nào ở đây nếu bạn đã có sẵn file diary.db
    }

    public void copyDatabase() {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (files != null) {
            for (String filename : files) {
                if (filename.equals(DATABASE_NAME)) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = assetManager.open(filename);
                        File outFile = context.getDatabasePath(DATABASE_NAME);
                        out = new FileOutputStream(outFile);
                        copyFile(in, out);
                        in.close();
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public <T> void Add(T object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Sử dụng reflection để lấy tên và giá trị của các trường của đối tượng
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    values.put(fieldName, String.valueOf(fieldValue));
                }
            }
            // Thêm dữ liệu vào cơ sở dữ liệu
            db.insert(object.getClass().getSimpleName(), null, values);
            db.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> void UpdateById(T object, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Sử dụng reflection để lấy tên và giá trị của các trường của đối tượng
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    values.put(fieldName, String.valueOf(fieldValue));
                }
            }
            // Cập nhật dữ liệu vào cơ sở dữ liệu
            db.update(object.getClass().getSimpleName(), values, "id=?", new String[]{String.valueOf(id)});
            db.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Xóa dữ liệu dựa trên ID
    public <T> void DeleteById(Class<T> clazz, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(clazz.getSimpleName(), "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Tìm kiếm dữ liệu dựa trên ID
    public <T> T FindById(Class<T> clazz, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(clazz.getSimpleName(), null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        T object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        db.close();
        return object;
    }

    private <T> T CreateModelObjectFromCursor(Class<T> clazz, Cursor cursor) {
        try {
            T object = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                int columnIndex = cursor.getColumnIndex(fieldName);
                if (columnIndex != -1) {
                    Class<?> fieldType = field.getType();
                    if (fieldType == int.class) {
                        field.setInt(object, cursor.getInt(columnIndex));
                    } else if (fieldType == String.class) {
                        field.set(object, cursor.getString(columnIndex));
                    } // Các kiểu dữ liệu khác có thể được xử lý tương tự
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void UpdateByEntryId(T object, int entryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Sử dụng reflection để lấy tên và giá trị của các trường của đối tượng
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    values.put(fieldName, String.valueOf(fieldValue));
                }
            }
            // Cập nhật dữ liệu vào cơ sở dữ liệu
            db.update(object.getClass().getSimpleName(), values, "EntryId=?", new String[]{String.valueOf(entryId)});
            db.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> void DeleteByEntryId(Class<T> clazz, int entryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(clazz.getSimpleName(), "EntryId=?", new String[]{String.valueOf(entryId)});
        db.close();
    }

    public <T> T FindByEntryId(Class<T> clazz, int entryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(clazz.getSimpleName(), null, "EntryId=?", new String[]{String.valueOf(entryId)}, null, null, null);
        T object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        db.close();
        return object;
    }
}
