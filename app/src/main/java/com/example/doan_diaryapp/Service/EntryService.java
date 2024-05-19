package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryActivity;
import com.example.doan_diaryapp.Models.EntryEmotion;
import com.example.doan_diaryapp.Models.EntryPartner;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.EntryWeather;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.ui.home.MonthFragment;
import com.example.doan_diaryapp.ui.home.SpecificDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

    public List<Entry> getEntriesFromDatabase() {
        db = this.getReadableDatabase();
        String DATE = "";
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY SUBSTR(Date, 16, 4) || SUBSTR(Date, 13, 2) || SUBSTR(Date, 10, 2)||SUBSTR(Date, 1, 2) || '-' || SUBSTR(Date, 4, 2) || '-' || SUBSTR(Date, 7, 2) DESC", null)) {
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
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }


    public List<Entry> getEntriesFromDatabase(String time) {
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY SUBSTR(Date, 16, 4) || SUBSTR(Date, 13, 2) || SUBSTR(Date, 10, 2)||SUBSTR(Date, 1, 2) || '-' || SUBSTR(Date, 4, 2) || '-' || SUBSTR(Date, 7, 2) DESC", null)) {
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
                    if (day.equals(time)) {
                        entryList.add(new Entry(id, note, date, title));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    private MaterialCalendarView calendarView;
    public String[]  getEntries() {
        db = this.getReadableDatabase();
        String[] Diary;
        Diary = new String[0];
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                do {
                    String date = cursor.getString(dateColumnIndex);
                    String day=date.substring(date.length() - 10);

                    String[] newArray = new String[Diary.length + 1];
                    System.arraycopy(Diary, 0, newArray, 0, Diary.length);
                    newArray[Diary.length] =day;
                    Diary = newArray;

                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return Diary;
    }


    EntryEmotionService entryEmotionService;
    EntryActivityService entryActivityService;
    EntryPartnerService entryPartnerService;
    EntryPhotoService entryPhotoService;
    EntryWeatherService entryWeatherService;
    ImportantDayService importantDayService;


    public void deleteDiary(String DATE,Context context) {
        db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int idColumnIndex = cursor.getColumnIndex("Id");
                do {
                    String date = cursor.getString(dateColumnIndex);
                    int id = cursor.getInt(idColumnIndex);
                    if (DATE.equals(date)){

                        entryPhotoService = new EntryPhotoService(context);
                        entryPhotoService.DeleteByEntryId(EntryPhoto.class, id);

                        entryActivityService = new EntryActivityService(context);
                        entryActivityService.DeleteByEntryId(EntryActivity.class, id);

                        entryEmotionService = new EntryEmotionService(context);
                        entryEmotionService.DeleteByEntryId(EntryEmotion.class, id);

                        entryPartnerService=new EntryPartnerService(context);
                        entryPartnerService.DeleteByEntryId(EntryPartner.class, id);

                        //entryWeatherService=new EntryWeatherService(context);
                        //entryWeatherService.DeleteByEntryId(EntryWeather.class, id);

                        importantDayService = new ImportantDayService(context);
                        ImportantDay importantDay = importantDayService.FindByDate(new ImportantDay(),DATE);
                        if (importantDay != null) {
                            importantDayService.DeleteById(ImportantDay.class, importantDay.getId());
                        }
                        db.delete("Entry", "Id = ?", new String[]{String.valueOf(id)});
                        return;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }




    public List<Entry>getOverallScoreByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date ASC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    int score = cursor.getInt(scoreColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Entry(score,date));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    public List<Entry>getOverallScoreByDayMonthYear(int day, int month, int year){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date ASC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int d = Integer.parseInt(parts[3]);
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    int score = cursor.getInt(scoreColumnIndex);
                    if(d == day && m == month && y == year){
                        entryList.add(new Entry(score,date));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    public List<Entry>getOverallScoreByYear(int year){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry ORDER BY Date ASC", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int y = Integer.parseInt(parts[5]);
                    int score = cursor.getInt(scoreColumnIndex);
                    if(y == year){
                        entryList.add(new Entry(score,date));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }

    public List<Entry>getOverallScoreCustom(int byear, int bmonth, int ayear, int amonth){
        db = this.getReadableDatabase();
        List<Entry> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Entry", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int scoreColumnIndex = cursor.getColumnIndex("OverallScore");
                int dateColumnIndex = cursor.getColumnIndex("Date");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int y = Integer.parseInt(parts[5]);
                    int m = Integer.parseInt(parts[4]);
                    int score = cursor.getInt(scoreColumnIndex);
                    boolean isAfterLower = (y > byear) || (y == byear && m >= bmonth);

                    boolean isBeforeUpper = (y < ayear) || (y == ayear && m <= amonth);

                    if(isBeforeUpper&&isAfterLower){
                        entryList.add(new Entry(score,date));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return entryList;
    }
}
