package com.example.doan_diaryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;
import com.hanks.passcodeview.PasscodeView;

public class OpenPasscodeView extends BaseActivity {

    PasscodeView passcodeView;
    String action;
    SharedPreferences sharedPreferences;
    Boolean passChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open_passcode_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Resources resources = getResources();

        passcodeView = findViewById(R.id.passcodeView1);
        passcodeView.setFirstInputTip(resources.getString(R.string.firstInputTip))
                .setSecondInputTip(resources.getString(R.string.secondInputTip))
                .setWrongLengthTip(resources.getString(R.string.wrongLengthTip))
                .setWrongInputTip(resources.getString(R.string.wrongInputTip))
                .setCorrectInputTip(resources.getString(R.string.correctInputTip));

        action = getIntent().getStringExtra("action");
        sharedPreferences = getSharedPreferences("Passcode", MODE_PRIVATE);
        passChange = sharedPreferences.getBoolean("passChange", false);

        boolean fromMainActivity = getIntent().getBooleanExtra("fromMainActivity", false);

        if (fromMainActivity) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            if ("create".equals(action)) {
                SetPassCode();
            } else if ("change".equals(action) || "delete".equals(action)) {
                VerifyPassCode();
            } else if ("verify".equals(action)) {
                VerifyPassword();
            }
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
                        Log.i("OpenPasscodeView", "Mật khẩu đúng, setpasscode");
                        sharedPreferences.edit().putString("passcode", number).apply();
                        if(!passChange){
                            NotificationService notificationService = new NotificationService(OpenPasscodeView.this);
                            notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 4, null, 1 ));
                        }
                        sharedPreferences.edit().putBoolean("passChange", false).apply();
                        Toast.makeText(getBaseContext(), R.string.loginAgain, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OpenPasscodeView.this, SettingActivity.class));
                        finish();
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
                            NotificationService notificationService = new NotificationService(OpenPasscodeView.this);
                            notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 5, null, 1 ));
                            sharedPreferences.edit().putBoolean("passChange", true).apply();
                            intent.putExtra("action", "create");
                            startActivity(intent);
                            finish();
                        }
                        else if ("delete".equals(action)) {
                            sharedPreferences.edit().remove("passcode").apply();
                            NotificationService notificationService = new NotificationService(OpenPasscodeView.this);
                            notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 6, null, 1 ));
                            //startActivity(new Intent(OpenPasscodeView.this, SettingActivity.class));
                            finish();
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
//                        finish();
                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent intent = new Intent(OpenPasscodeView.this, MainActivity.class);
                        boolean receivedBoolean = getIntent().getBooleanExtra("isChecked", true);
                        sharedPreferences.edit().putBoolean("mainIsChecked", receivedBoolean).apply();
                        intent.putExtra("isCheckMainActivity", true);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}