package com.example.doan_diaryapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ActivityNam extends BaseActivity {
    Button btnChangeLanguage, btnCancel, btnDisplayMode, btnShare, btnContact;

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

        // customDialog();

        btnDisplayMode = findViewById(R.id.btnDisplayMode);
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        btnShare = findViewById(R.id.btnShare);
        btnContact = findViewById(R.id.btnContact);

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
                // dialog.show();
                testCustomDialog(ActivityNam.this);
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

    private void testCustomDialog(Context context) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        String[] choices = {"Light", "Dark", "System default"};

        // Retrieve selected index from preferences
        int selectedIndex = getSelectedIndexFromPreferences();

        builder
                .setTitle(R.string.display_mode)
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss())
                .setSingleChoiceItems(choices, selectedIndex, (dialog, which) -> {
                    dialog.dismiss();
                    setNightModeAndRecreate(which);
                });

        builder.create().show();
    }

    private int getSelectedIndexFromPreferences() {
        // Retrieve stored index from SharedPreferences
        return getSharedPreferences("MODE", Context.MODE_PRIVATE).getInt("displayMode", 0);
    }

    private void setNightModeAndRecreate(int which) {
        AppCompatDelegate.setDefaultNightMode(getNightModeForIndex(which));

        // Refresh UI
        this.recreate();

        // Store selected index in SharedPreferences
        getSharedPreferences("MODE", Context.MODE_PRIVATE).edit()
                .putInt("displayMode", which)
                .apply();
    }

    private int getNightModeForIndex(int which) {
        switch (which) {
            case 0: return AppCompatDelegate.MODE_NIGHT_NO;
            case 1: return AppCompatDelegate.MODE_NIGHT_YES;
            default: return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
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