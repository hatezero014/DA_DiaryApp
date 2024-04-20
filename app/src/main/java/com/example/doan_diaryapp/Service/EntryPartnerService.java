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

    public List<Partner> getPartnerIdByMonthYear(int month, int year){
        db = this.getReadableDatabase();
        List<Partner> entryList = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM EntryPartner inner join Entry on EntryPartner.EntryId = Entry.Id inner join Partner on EntryPartner.PartnerId = Partner.Id", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int dateColumnIndex = cursor.getColumnIndex("Date");
                int iconColumnIndex = cursor.getColumnIndex("Icon");

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int m = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    if(m == month && y == year){
                        entryList.add(new Partner(icon));
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

                do {
                    String date = cursor.getString(dateColumnIndex).trim();
                    String[] parts = date.split("-");
                    int y = Integer.parseInt(parts[2]);
                    String icon = cursor.getString(iconColumnIndex);
                    if(y == year){
                        entryList.add(new Partner(icon));
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
