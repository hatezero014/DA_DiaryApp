package com.example.doan_diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hanks.passcodeview.PasscodeView;

public class OpenPasscodeView extends BaseActivity {

    PasscodeView passcodeView;
    String action;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open_passcode_view);

        passcodeView = findViewById(R.id.passcodeView1);
        action = getIntent().getStringExtra("action");
        sharedPreferences = getSharedPreferences("Passcode", MODE_PRIVATE);

        if ("create".equals(action)) {
            SetPassCode();
        } else if ("change".equals(action) || "delete".equals(action)) {
            VerifyPassCode();
        }
         else if ("verify".equals(action)) {
            VerifyPassword();
        }
    }

    private void SetPassCode() {
        passcodeView.setPasscodeLength(4)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Log.i("OpenPasscodeView", "Mật khẩu không đúng");
                    }

                    @Override
                    public void onSuccess(String number) {
                        Log.i("OpenPasscodeView", "Mật khẩu đúng");
                        sharedPreferences.edit().putString("passcode", number).apply();
                        startActivity(new Intent(OpenPasscodeView.this, PasswordActivity.class));
                    }
                });
    }

    private void VerifyPassCode() {
        final String savedPasscode = sharedPreferences.getString("passcode", "");
        passcodeView.setPasscodeLength(4)
                .setLocalPasscode(savedPasscode)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Log.i("OpenPasscodeView", "Mật khẩu không đúng");
                    }

                    @Override
                    public void onSuccess(String number) {
                        Log.i("OpenPasscodeView", "Mật khẩu đúng");
                        if ("change".equals(action)) {
                            Intent intent = new Intent(OpenPasscodeView.this, OpenPasscodeView.class);
                            intent.putExtra("action", "create");
                            startActivity(intent);
                        }
                        else if ("delete".equals(action)) {
                            sharedPreferences.edit().remove("passcode").apply();
                            startActivity(new Intent(OpenPasscodeView.this, PasswordActivity.class));
                        }
                    }
                });
    }

    private void VerifyPassword() {
        passcodeView.setPasscodeLength(4)
                .setLocalPasscode(sharedPreferences.getString("passcode", ""))
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        finish();
                    }

                    @Override
                    public void onSuccess(String number) {
                        startActivity(new Intent(OpenPasscodeView.this, MainActivity.class));
                        finish();
                    }
                });
    }
}