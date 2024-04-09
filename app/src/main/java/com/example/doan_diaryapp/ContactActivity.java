package com.example.doan_diaryapp;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ContactActivity extends BaseActivity {
    private ImageView imgFirst;
    Uri imgFiUri = null;
    RadioGroup radioGroupType, radioGroupCategory;
    int countImagesWithoutImage;
    private TextView textViewCategory;
    private ImageButton btnDeImgFi;
    private static final int PICK_IMAGES_REQUEST = 1;
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.contact_title));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroupType = findViewById(R.id.radioGroup_type);
        radioGroupCategory = findViewById(R.id.radioGroup_category);
        textViewCategory = findViewById(R.id.textView_category);

        imgFirst = findViewById(R.id.imgFi);
        btnDeImgFi = findViewById(R.id.btnDeImgFi);
        btnSend = findViewById(R.id.btnSendInquiry);

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton == findViewById(R.id.rBtnQuestion) || radioButton == findViewById(R.id.rBtnBugReportFi)) {
                    textViewCategory.setVisibility(View.VISIBLE);
                    radioGroupCategory.setVisibility(View.VISIBLE);
                }
                else {
                    radioGroupCategory.clearCheck();
                    textViewCategory.setVisibility(View.GONE);
                    radioGroupCategory.setVisibility(View.GONE);
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        btnDeImgFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFirst.setImageDrawable(null);
                btnDeImgFi.setVisibility(View.GONE);
                imgFiUri = null;
            }
        });

        Button btnAttach = findViewById(R.id.btnAttach);
        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countImagesWithoutImage = 0;

                if (imgFirst.getDrawable() == null) {
                    countImagesWithoutImage++;
                }

                if (countImagesWithoutImage == 0) {
                    showSnackbar(getString(R.string.contact_max_image));
                    return;
                }
                showSnackbar(getString(R.string.contact_allow_image));

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
            }
        });
    }

    void showSnackbar(String content) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                content, 2000);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }

    protected void sendEmail() {
        EditText editTextInquiry = findViewById(R.id.editText_inquiry);

        int selectedTypeId = radioGroupType.getCheckedRadioButtonId();
        RadioButton selectedTypeButton = findViewById(selectedTypeId);
        String subject = "";
        if (selectedTypeButton != null) {
            subject = selectedTypeButton.getText().toString();
        }

        int selectedCategoryId = radioGroupCategory.getCheckedRadioButtonId();
        RadioButton selectedCategoryButton = findViewById(selectedCategoryId);
        if (selectedCategoryButton != null) {
            if (!subject.isEmpty())
            {
                subject = subject + " - " + selectedCategoryButton.getText().toString();
            }
            else {
                subject = selectedCategoryButton.getText().toString();
            }
        }

        String inquiryText = editTextInquiry.getText().toString();

        String TO = "diaryappuit@gmail.com";
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, inquiryText);
        emailIntent.setSelector( selectorIntent );

        if (imgFiUri != null)
            emailIntent.putExtra(Intent.EXTRA_STREAM, imgFiUri);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(ContactActivity.this, ActivityNam.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    if (count > 1) {
                        showSnackbar(getString(R.string.contact_allow_image));
                        return;
                    }
                    Uri imageUri = clipData.getItemAt(0).getUri();
                    if (imageUri != null) {
                        if (imgFirst.getDrawable() == null) {
                            imgFirst.setImageURI(imageUri);
                            imgFiUri = imageUri;
                            btnDeImgFi.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }
}