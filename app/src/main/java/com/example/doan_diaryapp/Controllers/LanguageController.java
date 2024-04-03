package com.example.doan_diaryapp.Controllers;

import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan_diaryapp.BaseActivity;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.R;

import java.util.ArrayList;

public class LanguageController extends BaseActivity {
    public ArrayList<Language> getList() {
        ArrayList<Language> listLanguage = new ArrayList<>();

        Cursor c = database.query("Language",null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            listLanguage.add(new Language(c.getInt(0),c.getString(1), c.getString(2), c.getInt(3)));
            c.moveToNext();
        }

        return listLanguage;
    }
}
