package com.example.doan_diaryapp;

import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.ImageRecordAdapter;
import com.example.doan_diaryapp.Decorator.GridSpacingItemDecoration;

import java.util.Arrays;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecordActivity extends BaseActivity {

    private final List<Integer> imageMoodList = Arrays.asList(
            R.drawable.emoji_mood_joyful, R.drawable.emoji_mood_cool, R.drawable.emoji_mood_melting, R.drawable.emoji_mood_pleased,
            R.drawable.emoji_mood_happy, R.drawable.emoji_mood_surprise, R.drawable.emoji_mood_embarrassed, R.drawable.emoji_mood_normal,
            R.drawable.emoji_mood_fearful, R.drawable.emoji_mood_tired, R.drawable.emoji_mood_worried, R.drawable.emoji_mood_sad,
            R.drawable.emoji_mood_tired, R.drawable.emoji_mood_sleepy, R.drawable.emoji_mood_sick, R.drawable.emoji_mood_bored,
            R.drawable.emoji_mood_annoyed, R.drawable.emoji_mood_angry
    );

    private final List<Integer> imageActivityList = Arrays.asList(
            R.drawable.emoji_activity_work, R.drawable.emoji_activity_study, R.drawable.emoji_activity_bake, R.drawable.emoji_activity_write,
            R.drawable.emoji_activity_sport, R.drawable.emoji_activity_gym, R.drawable.emoji_activity_watch_movie, R.drawable.emoji_activity_game,
            R.drawable.emoji_activity_play_instruments, R.drawable.emoji_activity_sing, R.drawable.emoji_activity_listen_to_music, R.drawable.emoji_activity_shopping,
            R.drawable.emoji_activity_paint, R.drawable.emoji_activity_party, R.drawable.emoji_activity_photograph, R.drawable.emoji_activity_sleep,
            R.drawable.emoji_activity_play_cards, R.drawable.emoji_activity_cook, R.drawable.emoji_activity_housework, R.drawable.emoji_activity_read
    );

    private final List<Integer> imageCompanionList = Arrays.asList(
            R.drawable.emoji_companion_partner, R.drawable.emoji_companion_friends, R.drawable.emoji_companion_family, R.drawable.emoji_companion_pets
    );

    private Button bedtimeButton, wakeupButton;
    private boolean isBedtime;
    float sliderValue = 5;
    private ImageButton btnDeImgFi, btnDeImgSe, btnDeImgTh;
    private ImageView imgFirst, imgSecond, imgThird;
    Uri imgFiUri = null, imgSeUri = null, imgThUri = null;
    int hourWakeUp, hourBed, minWakeUp, minBed;
    int countImagesWithoutImage = 0;
    Drawable targetDrawable;
    private static final int PICK_IMAGES_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_share));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgFirst = findViewById(R.id.imageFirst);
        imgSecond = findViewById(R.id.imageSecond);
        imgThird = findViewById(R.id.imageThird);
        btnDeImgFi = findViewById(R.id.btnDeImgFi);
        btnDeImgSe = findViewById(R.id.btnDeImgSe);
        btnDeImgTh = findViewById(R.id.btnDeImgTh);
        targetDrawable = imgFirst.getDrawable();

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter1 = new ImageRecordAdapter(imageMoodList);
        recyclerView1.setAdapter(adapter1);
        recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter2 = new ImageRecordAdapter(imageActivityList);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter3 = new ImageRecordAdapter(imageCompanionList);
        recyclerView3.setAdapter(adapter3);
        recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        bedtimeButton = findViewById(R.id.bedtimeButton);
        wakeupButton = findViewById(R.id.wakeupButton);

        List<Integer> selectedItems1 = adapter1.getSelectedItems();
        List<Integer> selectedItems2 = adapter2.getSelectedItems();
        List<Integer> selectedItems3 = adapter3.getSelectedItems();

        Slider slider = findViewById(R.id.slider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                // Cập nhật giá trị của biến sliderValue
                sliderValue = value;
            }
        });

        bedtimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBedtime = true;
                String time = bedtimeButton.getText().toString();
                showTimePickerDialog(time);
            }
        });

        wakeupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBedtime = false;
                String time = wakeupButton.getText().toString();
                showTimePickerDialog(time);
            }
        });

        btnDeImgFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFirst.setImageDrawable(null);
                btnDeImgFi.setVisibility(View.GONE);
                imgFiUri = null;

                if (imgSeUri != null) {
                    imgFirst.setImageDrawable(imgSecond.getDrawable());
                    imgSecond.setImageDrawable(null);
                    btnDeImgFi.setVisibility(View.VISIBLE);
                    btnDeImgSe.setVisibility(View.GONE);
                    imgSecond.setVisibility(View.GONE);
                    imgFiUri = imgSeUri;
                    imgSeUri = null;
                }

                if (imgThUri != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setImageDrawable(null);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                    btnDeImgTh.setVisibility(View.GONE);
                    imgThird.setVisibility(View.GONE);
                    imgSeUri = imgThUri;
                    imgThUri = null;
                }

                if (imgFiUri == null) {
                    imgFirst.setImageDrawable(targetDrawable);
                }
            }
        });

        btnDeImgSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSecond.setImageDrawable(null);
                imgSecond.setVisibility(View.GONE);
                btnDeImgSe.setVisibility(View.GONE);
                imgSeUri = null;

                if (imgThUri != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setImageDrawable(null);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                    btnDeImgTh.setVisibility(View.GONE);
                    imgThird.setVisibility(View.GONE);
                    imgSeUri = imgThUri;
                    imgThUri = null;
                }
            }
        });

        btnDeImgTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgThird.setImageDrawable(null);
                imgThird.setVisibility(View.GONE);
                btnDeImgTh.setVisibility(View.GONE);
                imgThUri = null;
            }
        });
    }
    private void showTimePickerDialog(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);

        // Tạo dialog TimePicker với chế độ "spinner"
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                R.style.MyTimePickerDialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isBedtime) {
                            hourBed = hourOfDay;
                            minBed = minute;
                            bedtimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                        } else {
                            hourWakeUp = hourOfDay;
                            minWakeUp = minute;
                            wakeupButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }
                },
                hour,
                minute,
                true
        );

        // Đặt mode của TimePicker thành Spinner
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    void showSnackBar(String content) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                content, 2000);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    if (count > 3) {
                        showSnackBar(getString(R.string.record_allow_image));
                        count = 3;
                    }
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        if (imgFirst.getDrawable() == targetDrawable) {
                            imgFirst.setImageURI(imageUri);
                            imgFirst.setVisibility(View.VISIBLE);
                            imgFiUri = imageUri;
                            btnDeImgFi.setVisibility(View.VISIBLE);
                        }
                        else {
                            if (imgSecond.getDrawable() == null) {
                                imgSecond.setImageURI(imageUri);
                                imgSecond.setVisibility(View.VISIBLE);
                                imgSeUri = imageUri;
                                btnDeImgSe.setVisibility(View.VISIBLE);
                            }
                            else {
                                if (imgThird.getDrawable() == null) {
                                    imgThird.setImageURI(imageUri);
                                    imgThird.setVisibility(View.VISIBLE);
                                    imgThUri = imageUri;
                                    btnDeImgTh.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_record, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addphoto) {
            showSnackBar(getString(R.string.record_allow_image));

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
        }
        if (id == R.id.action_favorite) {
            boolean isSelected = item.isChecked();

            isSelected = !isSelected;

            item.setChecked(isSelected);

            if (isSelected) {
                item.setIcon(R.drawable.state_filled_record_star);
            } else {
                item.setIcon(R.drawable.state_outlined_record_star);
            }
        }
        if (id == android.R.id.home) {
            Intent intent = new Intent(RecordActivity.this, ActivityNam.class);
            startActivity(intent);
        }
        return true;
    }
}