package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

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

        // Initially hide ChangePassword and DeletePassword buttons
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
        // Check if password is set
        if (savedPasscode != null) {
            // If password is set, hide CreatePassword button and show ChangePassword and DeletePassword buttons
            createPasswordButton.setVisibility(View.GONE);
            changePasswordButton.setVisibility(View.VISIBLE);
            deletePasswordButton.setVisibility(View.VISIBLE);
            Intent intent = new Intent(PasswordActivity.this, OpenPasscodeView.class);
            intent.putExtra("action", "verify");
            startActivity(intent);
            finish();
        } else {
            // If password is not set, show CreatePassword button and hide ChangePassword and DeletePassword buttons
            createPasswordButton.setVisibility(View.VISIBLE);
            changePasswordButton.setVisibility(View.GONE);
            deletePasswordButton.setVisibility(View.GONE);
        }
    }
}
