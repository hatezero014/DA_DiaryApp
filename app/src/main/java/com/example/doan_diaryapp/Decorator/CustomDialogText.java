package com.example.doan_diaryapp.Decorator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.doan_diaryapp.R;

public class CustomDialogText extends Dialog {
    private EditText editText;
    private Button btnOk, btnCancel;

    private EditText holderEditText;

    public CustomDialogText(Context context, EditText editText) {
        super(context);
        holderEditText = editText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_text);

        editText = findViewById(R.id.editText);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editText.getText().toString();
                holderEditText.setText(description);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setEditTextText(String text) {
        editText.setText(text);
    }
}
