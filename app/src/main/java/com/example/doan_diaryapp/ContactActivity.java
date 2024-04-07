package com.example.doan_diaryapp;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class ContactActivity extends BaseActivity {
    private ImageView imgFirst, imgSecond, imgThird;
    int countImagesWithoutImage;
    private ImageButton btnDeImgFi, btnDeImgSe, btnDeImgTh;
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

        imgFirst = findViewById(R.id.imgFi);
        imgSecond = findViewById(R.id.imgSe);
        imgThird = findViewById(R.id.imgTh);
        btnDeImgFi = findViewById(R.id.btnDeImgFi);
        btnDeImgSe = findViewById(R.id.btnDeImgSe);
        btnDeImgTh = findViewById(R.id.btnDeImgTh);
        btnSend = findViewById(R.id.btnSendInquiry);

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

                if (imgSecond.getDrawable() != null) {
                    imgFirst.setImageDrawable(imgSecond.getDrawable());
                    imgSecond.setImageDrawable(null);
                    btnDeImgSe.setVisibility(View.GONE);
                    btnDeImgFi.setVisibility(View.VISIBLE);
                }

                if (imgThird.getDrawable() != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgThird.setImageDrawable(null);
                    btnDeImgTh.setVisibility(View.GONE);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDeImgSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa imageSe và ẩn ImageButton của nó
                imgSecond.setImageDrawable(null);
                btnDeImgSe.setVisibility(View.GONE);

                if (imgThird.getDrawable() != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgThird.setImageDrawable(null);
                    btnDeImgTh.setVisibility(View.GONE);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDeImgTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa imageTh và ẩn ImageButton của nó
                imgThird.setImageDrawable(null);
                btnDeImgTh.setVisibility(View.GONE);
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
                if (imgSecond.getDrawable() == null) {
                    countImagesWithoutImage++;
                }
                if (imgThird.getDrawable() == null) {
                    countImagesWithoutImage++;
                }

                if (countImagesWithoutImage == 0) {
                    Snackbar.make(findViewById(android.R.id.content), "You have already selected maximum images", 3000).show();
                    return;
                }
                Toast.makeText(ContactActivity.this, "You can only select up to " + countImagesWithoutImage + " images", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String TO = "22520912@gm.uit.edu.vn";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + TO));
        // emailIntent.setData(Uri.parse("mailto:"));
        // emailIntent.setType("text/plain");

        // emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pocket Diary - Bug report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Pocket Diary");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            // finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
                    if (count > countImagesWithoutImage) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "You can only select up to " + countImagesWithoutImage + " images", 3000).show();
                        return;
                    }
                    for (int i = 0; i < count; i++) {
                        // Lấy URI của từng ảnh
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        // Gán ảnh vào ImageView theo thứ tự
                        if (imgFirst.getDrawable() == null) {
                            imgFirst.setImageURI(imageUri);
                            btnDeImgFi.setVisibility(View.VISIBLE);
                        } else if (imgSecond.getDrawable() == null) {
                            imgSecond.setImageURI(imageUri);
                            btnDeImgSe.setVisibility(View.VISIBLE);
                        } else if (imgThird.getDrawable() == null) {
                            imgThird.setImageURI(imageUri);
                            btnDeImgTh.setVisibility(View.VISIBLE);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }
}