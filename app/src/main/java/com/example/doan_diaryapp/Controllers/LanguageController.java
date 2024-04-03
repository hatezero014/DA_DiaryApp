package com.example.doan_diaryapp.Controllers;

import android.content.Context;
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
    public static String getSubText(Context context) {
        String subText = context.getString(R.string.language_en);
        return subText;
    }
}
