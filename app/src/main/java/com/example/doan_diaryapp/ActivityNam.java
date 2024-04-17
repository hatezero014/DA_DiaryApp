package com.example.doan_diaryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryActivity;
import com.example.doan_diaryapp.Models.EntryEmotion;
import com.example.doan_diaryapp.Models.EntryPartner;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.EntryWeather;
import com.example.doan_diaryapp.Service.EntryActivityService;
import com.example.doan_diaryapp.Service.EntryEmotionService;
import com.example.doan_diaryapp.Service.EntryPartnerService;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.Service.EntryWeatherService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ActivityNam extends BaseActivity {
    Button btnChangeLanguage, btnCancel, btnDisplayMode, btnShare, btnContact, btnRecord;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nam);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        EntryService entryService = new EntryService(this);
//        Entry entry = entryService.FindByDate(new Entry(),"16-04-2024");
//        if (entry != null) {
//            Log.i("Entry Id:", String.valueOf(entry.getId()));
//            Log.i("Entry Note:", entry.getNote());
//            Log.i("Entry Date:", entry.getDate());
//            Log.i("Entry Overall Score:", String.valueOf(entry.getOverallScore()));
//            Log.i("Entry Wake Up Time:", entry.getWakeUp());
//            Log.i("Entry Sleep Time:", entry.getSleep());
//        }
//
//        EntryPhotoService entryPhotoService = new EntryPhotoService(this);
//        ArrayList<EntryPhoto> entryPhotos = entryPhotoService.GetAllByEntryId(EntryPhoto.class, 1);
//        for (EntryPhoto entryPhoto : entryPhotos) {
//            if (entryPhoto != null) {
//                Log.i("Entry Id:", String.valueOf(entryPhoto.getEntryId()));
//                Log.i("Photo", entryPhoto.getPhoto());
//            }
//        }
//
//
//        EntryEmotionService entryEmotionService = new EntryEmotionService(this);
//        ArrayList<EntryEmotion> entryEmotions = entryEmotionService.GetAllByEntryId(EntryEmotion.class, 1);
//        for (EntryEmotion entryEmotion : entryEmotions) {
//            if (entryEmotion != null) {
//                Log.i("Entry Id:", String.valueOf(entryEmotion.getEntryId()));
//                Log.i("Emotion Id", String.valueOf(entryEmotion.getEmotionId()));
//            }
//        }
//
//        EntryActivityService entryActivityService = new EntryActivityService(this);
//        ArrayList<EntryActivity> entryActivities = entryActivityService.GetAllByEntryId(EntryActivity.class, 1);
//        for (EntryActivity entryActivitie : entryActivities) {
//            if (entryActivitie != null) {
//                Log.i("Entry Id:", String.valueOf(entryActivitie.getEntryId()));
//                Log.i("Activity Id", String.valueOf(entryActivitie.getActivityId()));
//            }
//        }
//
//        EntryPartnerService entryPartnerService = new EntryPartnerService(this);
//        ArrayList<EntryPartner> entryPartners = entryPartnerService.GetAllByEntryId(EntryPartner.class, 1);
//        for (EntryPartner entryPartner : entryPartners) {
//            if (entryPartner != null) {
//                Log.i("Entry Id:", String.valueOf(entryPartner.getEntryId()));
//                Log.i("Partner Id", String.valueOf(entryPartner.getPartnerId()));
//            }
//        }
//
//        EntryWeatherService entryWeatherService = new EntryWeatherService(this);
//        ArrayList<EntryWeather> entryWeathers = entryWeatherService.GetAllByEntryId(EntryWeather.class, 1);
//        for (EntryWeather entryWeather : entryWeathers) {
//            if (entryWeather != null) {
//                Log.i("Entry Id:", String.valueOf(entryWeather.getEntryId()));
//                Log.i("Weather Id", String.valueOf(entryWeather.getWeatherId()));
//            }
//        }

        customDialog();

        btnDisplayMode = findViewById(R.id.btnDisplayMode);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnShare = findViewById(R.id.btnShare);
        btnContact = findViewById(R.id.btnContact);
        btnRecord = findViewById(R.id.btnRecord);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNam.this, RecordActivity.class);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                intent.putExtra("Date", String.format(Locale.ENGLISH, "%02d-%02d-%04d", day, month, year));
                startActivity(intent);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNam.this, ShareActivity.class);
                startActivity(intent);
                intent.putExtra("Month", 4);
                intent.putExtra("Year", 2024);
                startActivity(intent);
            }
        });

        btnDisplayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNam.this, ChangeLanguage.class);
                startActivity(intent);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNam.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    public void customDialog() {
        
        dialog = new Dialog(ActivityNam.this);
        dialog.setContentView(R.layout.dialog_display_mode);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_display_mode));
        dialog.setCancelable(true);

        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RadioButton rBtnLight = dialog.findViewById(R.id.rBtnLight);
        RadioButton rBtnDark = dialog.findViewById(R.id.rBtnDark);
        RadioButton rBtnSystem = dialog.findViewById(R.id.rBtnSystem);


        int displayMode = getSharedPreferences("MODE", Context.MODE_PRIVATE).getInt("displayMode", 2);
        if (displayMode == 0) {
            rBtnLight.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (displayMode == 1){
            rBtnDark.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            rBtnSystem.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        rBtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 0);
                editor.apply();
                dialog.dismiss();
            }
        });

        rBtnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 1);
                editor.apply();
                dialog.dismiss();
            }
        });

        rBtnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 2);
                editor.apply();
                dialog.dismiss();
            }
        });
    }
}