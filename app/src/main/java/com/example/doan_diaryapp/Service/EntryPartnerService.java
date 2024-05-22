package com.example.doan_diaryapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.EntryEmotion;
import com.example.doan_diaryapp.Models.EntryPartner;
import com.example.doan_diaryapp.Models.Partner;

import java.util.ArrayList;
import java.util.List;

public class EntryPartnerService extends BaseService {
    public EntryPartnerService(Context context) {
        super(context);
    }

    public EntryPartner FindByEntryIdAndPartnerId(Class<EntryPartner> clazz, int entryId, int partnerId) {
        db = this.getReadableDatabase();
        String selection = "EntryId=? AND PartnerId=?";
        String[] selectionArgs = new String[]{String.valueOf(entryId), String.valueOf(partnerId)};
        Cursor cursor = db.query(clazz.getSimpleName(), null, selection, selectionArgs, null, null, null);
        EntryPartner object = null;
        if (cursor != null && cursor.moveToFirst()) {
            object = CreateModelObjectFromCursor(clazz, cursor);
            cursor.close();
        }
        return object;
    }
    public List<Partner> getPartnerIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Partner> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPartner inner join Entry on EntryPartner.EntryId = Entry.Id inner join Partner on EntryPartner.PartnerId = Partner.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Partner(icon, descEn, descVi));
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

    public List<Partner> getPartnerIdByYear(int year){
        db = this.getReadableDatabase();
        List<Partner> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPartner inner join Entry on EntryPartner.EntryId = Entry.Id inner join Partner on EntryPartner.PartnerId = Partner.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    if(y == year){
                        entryList.add(new Partner(icon, descEn, descVi));
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

    public List<Partner> getPartnerIdByDayMonthYear(int day, int month, int year){
        db = this.getReadableDatabase();
        List<Partner> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPartner inner join Entry on EntryPartner.EntryId = Entry.Id inner join Partner on EntryPartner.PartnerId = Partner.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int d = Integer.parseInt(parts[3]);
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);
                    if(d == day && m == month && y == year){
                        entryList.add(new Partner(icon, descEn, descVi));
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

    public List<Partner> getPartnerIdCustom(int byear, int bmonth, int ayear, int amonth){
        db = this.getReadableDatabase();
        List<Partner> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPartner inner join Entry on EntryPartner.EntryId = Entry.Id inner join Partner on EntryPartner.PartnerId = Partner.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");
                int descEnColumnIndex = cursor.getColumnIndex("DescEn");
                int descViColumnIndex = cursor.getColumnIndex("DescVi");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("[:\\s-]");
                    int m = Integer.parseInt(parts[4]);
                    int y = Integer.parseInt(parts[5]);
                    String icon = cursor.getString(iconColumnIndex);
                    String descEn = cursor.getString(descEnColumnIndex);
                    String descVi = cursor.getString(descViColumnIndex);

                    boolean isAfterLower = (y > byear) || (y == byear && m >= bmonth);

                    boolean isBeforeUpper = (y < ayear) || (y == ayear && m <= amonth);

                    if(isBeforeUpper && isAfterLower){
                        entryList.add(new Partner(icon, descEn, descVi));
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
