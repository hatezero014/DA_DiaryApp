package com.example.doan_diaryapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.hanks.passcodeview.PasscodeView;

public class PasswordActivity extends AppCompatActivity {

    private Button createPasswordButton, changePasswordButton, deletePasswordButton;
    private PasscodeView passcodeView;
    private String savedPasscode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        createPasswordButton = findViewById(R.id.CreatePassWord);
        changePasswordButton = findViewById(R.id.ChangePassWord);
        deletePasswordButton = findViewById(R.id.DeletePassWord);
        passcodeView = findViewById(R.id.passcodeView);

        createPasswordButton.setOnClickListener(v -> {
            passcodeView.setVisibility(View.VISIBLE);
            passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE);
            passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail() {
                }

                @Override
                public void onSuccess(String number) {
                    savedPasscode = number;
                    passcodeView.setVisibility(View.GONE);
                }
            });
        });

        changePasswordButton.setOnClickListener(v -> {
            if (savedPasscode != null) {
                passcodeView.setVisibility(View.VISIBLE);
                passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE);
                passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                    }

                    @Override
                    public void onSuccess(String number) {
                        if (number.equals(savedPasscode)) {
                            passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE);
                        }
                    }
                });
            }
        });

        deletePasswordButton.setOnClickListener(v -> {
            if (savedPasscode != null) {
                passcodeView.setVisibility(View.VISIBLE);
                passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE);
                passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                    }

                    @Override
                    public void onSuccess(String number) {
                        if (number.equals(savedPasscode)) {
                            savedPasscode = null;
                            passcodeView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}