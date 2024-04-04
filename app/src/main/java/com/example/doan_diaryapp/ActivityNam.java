package com.example.doan_diaryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityNam extends BaseActivity {
    Button btnChangeLanguage, btnCancel, btnDisplayMode, btnShare;

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

        customDialog();


        btnDisplayMode = findViewById(R.id.btnDisplayMode);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnShare = findViewById(R.id.btnShare);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNam.this, ShareActivity.class);
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
    }

    public void customDialog() {
        dialog = new Dialog(ActivityNam.this);
        dialog.setContentView(R.layout.dialog_display_mode);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_display_mode));
        dialog.setCancelable(true); // hình như là true mới đúng?

        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RadioButton rbtnLight = dialog.findViewById(R.id.rbtnLight);
        RadioButton rbtnDark = dialog.findViewById(R.id.rbtnDark);
        RadioButton rbtnSystem = dialog.findViewById(R.id.rbtnSystem);


        int displayMode = getSharedPreferences("MODE", Context.MODE_PRIVATE).getInt("displayMode", 0);
        if (displayMode == 0) {
            rbtnLight.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (displayMode == 1){
            rbtnDark.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            rbtnSystem.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        rbtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 0);
                editor.apply();
                dialog.dismiss();
            }
        });

        rbtnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 1);
                editor.apply();
                dialog.dismiss();
            }
        });

        rbtnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                SharedPreferences.Editor editor = getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 2);
                editor.apply();
                // dialog.dismiss();
            }
        });
    }
}