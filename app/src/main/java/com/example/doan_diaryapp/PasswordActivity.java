package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;

public class PasswordActivity extends BaseActivity {

    private Button createPasswordButton, changePasswordButton, deletePasswordButton;
    // String savedPasscode = null;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        createPasswordButton = findViewById(R.id.CreatePassWord);
        changePasswordButton = findViewById(R.id.ChangePassWord);
        deletePasswordButton = findViewById(R.id.DeletePassWord);
        sharedPreferences = getSharedPreferences("Passcode", MODE_PRIVATE);


        changePasswordButton.setVisibility(View.GONE);
        deletePasswordButton.setVisibility(View.GONE);

        createPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "create");
                startActivity(intent);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "change");
                startActivity(intent);
            }
        });

        deletePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, OpenPasscodeView.class);
                intent.putExtra("action", "delete");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String savedPasscode = sharedPreferences.getString("passcode", null);
        if (savedPasscode != null) {
            createPasswordButton.setVisibility(View.GONE);
            changePasswordButton.setVisibility(View.VISIBLE);
            deletePasswordButton.setVisibility(View.VISIBLE);
            Intent intent = new Intent(PasswordActivity.this, OpenPasscodeView.class);
            intent.putExtra("action", "verify");
            startActivity(intent);
            finish();
        } else {
            createPasswordButton.setVisibility(View.VISIBLE);
            changePasswordButton.setVisibility(View.GONE);
            deletePasswordButton.setVisibility(View.GONE);
        }
    }
}
