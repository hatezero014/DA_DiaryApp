package com.example.doan_diaryapp;

import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
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
import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryActivity;
import com.example.doan_diaryapp.Models.EntryEmotion;
import com.example.doan_diaryapp.Models.EntryPartner;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.EntryWeather;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.Models.Partner;
import com.example.doan_diaryapp.Models.Weather;
import com.example.doan_diaryapp.Service.ActivityService;
import com.example.doan_diaryapp.Service.EmotionService;
import com.example.doan_diaryapp.Service.EntryActivityService;
import com.example.doan_diaryapp.Service.EntryEmotionService;
import com.example.doan_diaryapp.Service.EntryPartnerService;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.Service.EntryWeatherService;
import com.example.doan_diaryapp.Service.ImportantDayService;
import com.example.doan_diaryapp.Service.PartnerService;
import com.example.doan_diaryapp.Service.WeatherService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecordActivity extends BaseActivity {

    private OnBackPressedDispatcher dispatcher;

    private List<Integer> imageMoodList = Arrays.asList(
            R.drawable.emoji_emotion_joyful, R.drawable.emoji_emotion_cool, R.drawable.emoji_emotion_melting, R.drawable.emoji_emotion_pleased,
            R.drawable.emoji_emotion_happy, R.drawable.emoji_emotion_surprise, R.drawable.emoji_emotion_embarrassed, R.drawable.emoji_emotion_normal,
            R.drawable.emoji_emotion_fearful, R.drawable.emoji_emotion_tired, R.drawable.emoji_emotion_worried, R.drawable.emoji_emotion_sad,
            R.drawable.emoji_emotion_disappointed, R.drawable.emoji_emotion_sleepy, R.drawable.emoji_emotion_sick, R.drawable.emoji_emotion_bored,
            R.drawable.emoji_emotion_annoyed, R.drawable.emoji_emotion_angry
    );

    private List<Integer> imageActivityList = Arrays.asList(
            R.drawable.emoji_activity_work, R.drawable.emoji_activity_study, R.drawable.emoji_activity_bake, R.drawable.emoji_activity_write,
            R.drawable.emoji_activity_sport, R.drawable.emoji_activity_gym, R.drawable.emoji_activity_watch_movie, R.drawable.emoji_activity_game,
            R.drawable.emoji_activity_play_instruments, R.drawable.emoji_activity_sing, R.drawable.emoji_activity_biking, R.drawable.emoji_activity_shopping,
            R.drawable.emoji_activity_paint, R.drawable.emoji_activity_party, R.drawable.emoji_activity_photograph, R.drawable.emoji_activity_sleep,
            R.drawable.emoji_activity_play_cards, R.drawable.emoji_activity_cook, R.drawable.emoji_activity_housework, R.drawable.emoji_activity_read
    );

    private List<Integer> imageCompanionList = Arrays.asList(
            R.drawable.emoji_companion_partner, R.drawable.emoji_companion_friends, R.drawable.emoji_companion_family, R.drawable.emoji_companion_pets
    );

    private final List<Integer> imageWeatherList = Arrays.asList(
            R.drawable.emoji_weather_cloudy, R.drawable.emoji_weather_rainbow, R.drawable.emoji_weather_partly_cloudy, R.drawable.emoji_weather_rainy,
            R.drawable.emoji_weather_sunny, R.drawable.emoji_weather_windy
    );

    private ImageButton btnDeImgFi, btnDeImgSe, btnDeImgTh;
    private ImageView imgFirst, imgSecond, imgThird;
    int countImage = 0;
    Boolean isCheckFavorite = false;
    Drawable targetDrawable;
    Uri imgFiUri = null, imgSeUri = null, imgThUri = null;
    String date;
    private static final int PICK_IMAGES_REQUEST = 1;
    Slider slider;
    TextView textNode, textCount;
    Button btnDone;
    boolean checkChangedImage = false;
    List<Integer> partnerIndexes, weatherIndexes, emotionIndexes, activityIndexes;
    ImageRecordAdapter adapter1, adapter2, adapter3, adapter4;
    Entry result;
    EntryService entryService;
    EntryEmotionService entryEmotionService;
    EntryActivityService entryActivityService;
    EntryPartnerService entryPartnerService;
    EntryPhotoService entryPhotoService;
    EntryWeatherService entryWeatherService;
    ActivityService activityService;
    EmotionService emotionService;
    PartnerService partnerService;
    WeatherService weatherService;
    ImportantDayService importantDayService;

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

        dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isDataChanged())
                    showDialogAlert();
                else finish();
            }
        });

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");

        entryService = new EntryService(this);
        entryEmotionService = new EntryEmotionService(this);
        entryActivityService = new EntryActivityService(this);
        entryPartnerService = new EntryPartnerService(this);
        entryPhotoService = new EntryPhotoService(this);
        entryWeatherService = new EntryWeatherService(this);
        activityService = new ActivityService(this);
        emotionService = new EmotionService(this);
        partnerService = new PartnerService(this);
        weatherService = new WeatherService(this);
        importantDayService = new ImportantDayService(this);

        imgFirst = findViewById(R.id.imageFirst);
        imgSecond = findViewById(R.id.imageSecond);
        imgThird = findViewById(R.id.imageThird);
        btnDeImgFi = findViewById(R.id.btnDeImgFi);
        btnDeImgSe = findViewById(R.id.btnDeImgSe);
        btnDeImgTh = findViewById(R.id.btnDeImgTh);
        btnDone = findViewById(R.id.btnDone);
        targetDrawable = imgFirst.getDrawable();
        textNode = findViewById(R.id.textNote);
        slider = findViewById(R.id.slider);
        textCount = findViewById(R.id.txtCountImage);
        textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 4));

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 4));

        RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView4.setLayoutManager(new GridLayoutManager(this, 4));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(date);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Settings", android.app.Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Language", "");

        List<String> descMoodList = new ArrayList<>();
        for (Integer resourceId : imageMoodList) {
            String resourceName = getResourceName(resourceId);
            Emotion emotion = emotionService.FindByIcon(Emotion.class, resourceName);
            String desc = emotion.getDescEn();
            if (language.equals("vi"))
                desc = emotion.getDescVi();
            descMoodList.add(desc);
        }

        List<String> descActivityList = new ArrayList<>();
        for (Integer resourceId : imageActivityList) {
            String resourceName = getResourceName(resourceId);
            Activity activity = activityService.FindByIcon(Activity.class, resourceName);
            String desc = activity.getDescEn();
            if (language.equals("vi"))
                desc = activity.getDescVi();
            descActivityList.add(desc);
        }

        List<String> descPartnerList = new ArrayList<>();
        for (Integer resourceId : imageCompanionList) {
            String resourceName = getResourceName(resourceId);
            Partner partner = partnerService.FindByIcon(Partner.class, resourceName);
            String desc = partner.getDescEn();
            if (language.equals("vi"))
                desc = partner.getDescVi();
            descPartnerList.add(desc);
        }

        List<String> descWeatherList = new ArrayList<>();
        for (Integer resourceId : imageWeatherList) {
            String resourceName = getResourceName(resourceId);
            Weather weather = weatherService.FindByIcon(Weather.class, resourceName);
            String desc = weather.getDescEn();
            if (language.equals("vi"))
                desc = weather.getDescVi();
            descWeatherList.add(desc);
        }

        result = entryService.FindByDate(new Entry(), date);
        if (result != null) {
            textNode.setText(result.getNote());
            slider.setValue((float)result.getOverallScore());
            ArrayList<EntryActivity> entryActivities = entryActivityService.GetAllByEntryId(EntryActivity.class, result.getId());
            ArrayList<EntryEmotion> entryEmotions = entryEmotionService.GetAllByEntryId(EntryEmotion.class, result.getId());
            ArrayList<EntryPartner> entryPartners = entryPartnerService.GetAllByEntryId(EntryPartner.class, result.getId());
            ArrayList<EntryWeather> entryWeathers = entryWeatherService.GetAllByEntryId(EntryWeather.class, result.getId());
            ArrayList<EntryPhoto> entryPhotos = entryPhotoService.GetAllByEntryId(EntryPhoto.class, result.getId());

            activityIndexes = new ArrayList<>();
            for (EntryActivity entity : entryActivities) {
                Activity activity = activityService.FindById(Activity.class, entity.getActivityId());
                int index = imageActivityList.indexOf(getDrawableResourceId(this, activity.getIcon()));
                if (index != -1) {
                    activityIndexes.add(index);
                }
            }

            emotionIndexes = new ArrayList<>();
            for (EntryEmotion entity : entryEmotions) {
                Emotion emotion = emotionService.FindById(Emotion.class, entity.getEmotionId());
                int index = imageMoodList.indexOf(getDrawableResourceId(this, emotion.getIcon()));
                if (index != -1) {
                    emotionIndexes.add(index);
                }
            }

            partnerIndexes = new ArrayList<>();
            for (EntryPartner entity : entryPartners) {
                Partner partner = partnerService.FindById(Partner.class, entity.getPartnerId());
                int index = imageCompanionList.indexOf(getDrawableResourceId(this, partner.getIcon()));
                if (index != -1) {
                    partnerIndexes.add(index);
                }
            }

            weatherIndexes = new ArrayList<>();
            for (EntryWeather entity : entryWeathers) {
                Weather weather = weatherService.FindById(Weather.class, entity.getWeatherId());
                int index = imageWeatherList.indexOf(getDrawableResourceId(this, weather.getIcon()));
                if (index != -1) {
                    weatherIndexes.add(index);
                }
            }

            adapter1 = new ImageRecordAdapter(imageMoodList, descMoodList, emotionIndexes);
            recyclerView1.setAdapter(adapter1);
            recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter2 = new ImageRecordAdapter(imageActivityList, descActivityList, activityIndexes);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter3 = new ImageRecordAdapter(imageCompanionList, descPartnerList, partnerIndexes);
            recyclerView3.setAdapter(adapter3);
            recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter4 = new ImageRecordAdapter(imageWeatherList, descWeatherList, weatherIndexes);
            recyclerView4.setAdapter(adapter4);
            recyclerView4.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

            for (int i = 0; i < entryPhotos.size(); i++) {
                EntryPhoto entryPhoto = entryPhotos.get(i);
                String relativePath = entryPhoto.getPhoto();
                File file = new File(this.getFilesDir(), relativePath);
                String absolutePath = file.getAbsolutePath();

                if (i == 0) {
                    Picasso.get().load(new File(absolutePath)).into(imgFirst);
                    countImage++;
                    btnDeImgFi.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    Picasso.get().load(new File(absolutePath)).into(imgSecond);
                    imgSecond.setVisibility(View.VISIBLE);
                    countImage++;
                    btnDeImgSe.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    Picasso.get().load(new File(absolutePath)).into(imgThird);
                    imgThird.setVisibility(View.VISIBLE);
                    countImage++;
                    btnDeImgTh.setVisibility(View.VISIBLE);
                }
            };
            textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));

            ImportantDay importantDay = importantDayService.FindByDate(new ImportantDay(), date);
            if (importantDay != null) {
                isCheckFavorite = true;
                invalidateOptionsMenu();
            }
        }
        else {
            adapter1 = new ImageRecordAdapter(imageMoodList, descMoodList, null);
            recyclerView1.setAdapter(adapter1);
            recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter2 = new ImageRecordAdapter(imageActivityList, descActivityList, null);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter3 = new ImageRecordAdapter(imageCompanionList, descPartnerList, null);
            recyclerView3.setAdapter(adapter3);
            recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter4 = new ImageRecordAdapter(imageWeatherList, descWeatherList, null);
            recyclerView4.setAdapter(adapter4);
            recyclerView4.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
        }

        btnDeImgFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFirst.setImageDrawable(null);
                btnDeImgFi.setVisibility(View.GONE);
                imgFiUri = null;
                countImage--;
                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                checkChangedImage = true;

                if (imgSecond.getDrawable() != null) {
                    imgFirst.setImageDrawable(imgSecond.getDrawable());
                    imgSecond.setImageDrawable(null);
                    btnDeImgFi.setVisibility(View.VISIBLE);
                    btnDeImgSe.setVisibility(View.GONE);
                    imgSecond.setVisibility(View.GONE);
                    imgFiUri = imgSeUri;
                    imgSeUri = null;
                }

                if (imgThird.getDrawable() != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setImageDrawable(null);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                    btnDeImgTh.setVisibility(View.GONE);
                    imgThird.setVisibility(View.GONE);
                    imgSeUri = imgThUri;
                    imgThUri = null;
                }

                if (imgFirst.getDrawable() == null) {
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
                countImage--;;
                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                checkChangedImage = true;

                if (imgThird.getDrawable() != null) {
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
                countImage--;
                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                checkChangedImage = true;
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String notes = textNode.getText().toString();
                    String wakeUp = "6:30";
                    String sleep = "6:30";
                    int overallScore = (int) slider.getValue();
                    List<Integer> selectedItems1 = adapter1.getSelectedItems();
                    List<Integer> selectedItems2 = adapter2.getSelectedItems();
                    List<Integer> selectedItems3 = adapter3.getSelectedItems();
                    List<Integer> selectedItems4 = adapter4.getSelectedItems();
                    Entry entity = new Entry(notes, date, overallScore, wakeUp, sleep);
                    if (result == null) {
                        entryService.Add(entity);
                        int id = entryService.FindByDate(new Entry(), date).getId();
                        for (Integer imageId : selectedItems1) {
                            String icon = getResources().getResourceEntryName(imageMoodList.get(imageId));
                            Emotion emotion = emotionService.GetByIcon(new Emotion(), icon);
                            entryEmotionService.Add(new EntryEmotion(id, emotion.getId()));
                        }

                        for (Integer imageId : selectedItems2) {
                            String icon = getResources().getResourceEntryName(imageActivityList.get(imageId));
                            Activity activity = activityService.GetByIcon(new Activity(), icon);
                            entryActivityService.Add(new EntryActivity(id, activity.getId()));
                        }

                        for (Integer imageId : selectedItems3) {
                            String icon = getResources().getResourceEntryName(imageCompanionList.get(imageId));
                            Partner partner = partnerService.GetByIcon(new Partner(), icon);
                            entryPartnerService.Add(new EntryPartner(id, partner.getId()));
                        }

                        for (Integer imageId : selectedItems4) {
                            String icon = getResources().getResourceEntryName(imageWeatherList.get(imageId));
                            Weather weather = weatherService.GetByIcon(new Weather(), icon);
                            entryWeatherService.Add(new EntryWeather(id, weather.getId()));
                        }

                        if (imgFirst.getDrawable() != targetDrawable) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgFirst);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }

                        if (imgSecond.getDrawable() != null) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgSecond);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }

                        if (imgThird.getDrawable() != null) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgThird);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }
                    }
                    else {
                        int id = result.getId();
                        entryService.UpdateById(entity, id);
                        entryPhotoService.DeleteByEntryId(EntryPhoto.class, id);
                        entryActivityService.DeleteByEntryId(EntryActivity.class, id);
                        entryEmotionService.DeleteByEntryId(EntryEmotion.class, id);
                        entryPartnerService.DeleteByEntryId(EntryPartner.class, id);
                        entryWeatherService.DeleteByEntryId(EntryWeather.class, id);

                        for (Integer imageId : selectedItems1) {
                            String icon = getResources().getResourceEntryName(imageMoodList.get(imageId));
                            Emotion emotion = emotionService.GetByIcon(new Emotion(), icon);
                            entryEmotionService.Add(new EntryEmotion(id, emotion.getId()));
                        }

                        for (Integer imageId : selectedItems2) {
                            String icon = getResources().getResourceEntryName(imageActivityList.get(imageId));
                            Activity activity = activityService.GetByIcon(new Activity(), icon);
                            entryActivityService.Add(new EntryActivity(id, activity.getId()));
                        }

                        for (Integer imageId : selectedItems3) {
                            String icon = getResources().getResourceEntryName(imageCompanionList.get(imageId));
                            Partner partner = partnerService.GetByIcon(new Partner(), icon);
                            entryPartnerService.Add(new EntryPartner(id, partner.getId()));
                        }

                        for (Integer imageId : selectedItems4) {
                            String icon = getResources().getResourceEntryName(imageWeatherList.get(imageId));
                            Weather weather = weatherService.GetByIcon(new Weather(), icon);
                            entryWeatherService.Add(new EntryWeather(id, weather.getId()));
                        }

                        if (imgFirst.getDrawable() != targetDrawable) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgFirst);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }

                        if (imgSecond.getDrawable() != null) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgSecond);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }

                        if (imgThird.getDrawable() != null) {
                            String relativePath = saveImageToAppDirectory(RecordActivity.this, imgThird);
                            entryPhotoService.Add(new EntryPhoto(id, relativePath));
                        }
                    }
                    Toast.makeText(RecordActivity.this, R.string.record_toast_success, Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (Exception e) {
                    Toast.makeText(RecordActivity.this, R.string.record_toast_fail, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private String getResourceName(int resourceId) {
        return getResources().getResourceEntryName(resourceId);
    }

    private int getDrawableResourceId(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    void showSnackBar(String content) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                content, BaseTransientBottomBar.LENGTH_SHORT);

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
                    if (countImage + count > 3) {
                        showSnackBar(getString(R.string.record_allow_image));
                        count = 3 - countImage;;
                    }
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        if (imgFirst.getDrawable() == targetDrawable) {
                            imgFirst.setImageURI(imageUri);
                            imgFirst.setVisibility(View.VISIBLE);
                            imgFiUri = imageUri;
                            btnDeImgFi.setVisibility(View.VISIBLE);
                            checkChangedImage = true;
                            countImage++;
                            textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                        }
                        else {
                            if (imgSecond.getDrawable() == null) {
                                imgSecond.setImageURI(imageUri);
                                imgSecond.setVisibility(View.VISIBLE);
                                imgSeUri = imageUri;
                                btnDeImgSe.setVisibility(View.VISIBLE);
                                checkChangedImage = true;
                                countImage++;;
                                textCount.setText(String.format(Locale.ENGLISH, "%d/%d", countImage, 3));
                            }
                            else {
                                if (imgThird.getDrawable() == null) {
                                    imgThird.setImageURI(imageUri);
                                    imgThird.setVisibility(View.VISIBLE);
                                    imgThUri = imageUri;
                                    checkChangedImage = true;
                                    btnDeImgTh.setVisibility(View.VISIBLE);
                                    countImage++;;
                                    textCount.setText(String.format(Locale.ENGLISH, "%d/%d", countImage, 3));
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
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        if (favoriteItem != null) {
            boolean isSelected = isCheckFavorite;
            if (isSelected) {
                favoriteItem.setIcon(R.drawable.state_filled_record_star);
                favoriteItem.setChecked(true); // Đặt trạng thái đã chọn
            } else {
                favoriteItem.setIcon(R.drawable.state_outlined_record_star);
                favoriteItem.setChecked(false); // Đặt trạng thái không được chọn
            }
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addphoto) {
            // showSnackBar(getString(R.string.record_allow_image));

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
        }
        if (id == R.id.action_favorite) {
            boolean isSelected = item.isChecked();
            ImportantDayService importantDayService = new ImportantDayService(this);

            isSelected = !isSelected;

            item.setChecked(isSelected);

            if (isSelected) {
                item.setIcon(R.drawable.state_filled_record_star);
                importantDayService.Add(new ImportantDay(date));
            } else {
                int idDay = importantDayService.FindByDate(new ImportantDay(), date).getId();
                importantDayService.DeleteById(ImportantDay.class, idDay);
                item.setIcon(R.drawable.state_outlined_record_star);
            }
        }
        if (id == android.R.id.home) {
            if (isDataChanged())
                showDialogAlert();
            else finish();
        }
        return true;
    }

    private boolean isDataChanged() {
        String notes = textNode.getText().toString();
        String wakeUp = "6:30";
        String sleep = "6:30";
        int overallScore = (int) slider.getValue();
        List<Integer> selectedItems1 = adapter1.getSelectedItems();
        List<Integer> selectedItems2 = adapter2.getSelectedItems();
        List<Integer> selectedItems3 = adapter3.getSelectedItems();
        List<Integer> selectedItems4 = adapter4.getSelectedItems();
        Entry entity = new Entry(notes, date, overallScore, wakeUp, sleep);
        if (result == null) {
            return !notes.isEmpty() || overallScore != 5
                    || !selectedItems1.isEmpty() || !selectedItems2.isEmpty()
                    || !selectedItems3.isEmpty() || !selectedItems4.isEmpty();
        }
        boolean checkChanged = false;
        if (result.getOverallScore() == entity.getOverallScore()
                && result.getDate().equals(entity.getDate())
                && result.getSleep().equals(entity.getSleep())
                && result.getNote().equals(entity.getNote())
                && result.getWakeUp().equals(entity.getWakeUp())) {
            int entryId = result.getId();
            ArrayList<EntryEmotion> entryEmotions = entryEmotionService.GetAllByEntryId(EntryEmotion.class, entryId);
            ArrayList<EntryActivity> entryActivities = entryActivityService.GetAllByEntryId(EntryActivity.class, entryId);
            ArrayList<EntryPartner> entryPartners = entryPartnerService.GetAllByEntryId(EntryPartner.class, entryId);
            ArrayList<EntryWeather> entryWeathers = entryWeatherService.GetAllByEntryId(EntryWeather.class, entryId);
            if (entryEmotions.size() == selectedItems1.size()
                    && entryActivities.size() == selectedItems2.size()
                    && entryPartners.size() == selectedItems3.size()
                    && entryWeathers.size() == selectedItems4.size()) {
                for (Integer imageId : selectedItems1) {
                    String icon = getResources().getResourceEntryName(imageMoodList.get(imageId));
                    Emotion emotion = emotionService.GetByIcon(new Emotion(), icon);
                    if (entryEmotionService.FindByEntryIdAndEmotionId(EntryEmotion.class, entryId, emotion.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems2) {
                    String icon = getResources().getResourceEntryName(imageActivityList.get(imageId));
                    Activity activity = activityService.GetByIcon(new Activity(), icon);
                    if (entryActivityService.FindByEntryIdAndActivityId(EntryActivity.class, entryId, activity.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems3) {
                    String icon = getResources().getResourceEntryName(imageCompanionList.get(imageId));
                    Partner partner = partnerService.GetByIcon(new Partner(), icon);
                    if (entryPartnerService.FindByEntryIdAndPartnerId(EntryPartner.class, entryId, partner.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems4) {
                    String icon = getResources().getResourceEntryName(imageWeatherList.get(imageId));
                    Weather weather = weatherService.GetByIcon(new Weather(), icon);
                    if (entryWeatherService.FindByEntryIdAndWeatherId(EntryWeather.class, entryId, weather.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }
                if (checkChangedImage)
                    checkChanged = true;
            }
            else {
                checkChanged = true;
            }
        }
        else {
            checkChanged = true;
        }
        return checkChanged;
    }

    private void showDialogAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setMessage(R.string.record_alert_message)
                .setTitle(R.string.record_alert_title)
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public String saveImageToAppDirectory(Context context, ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        File directory = new File(context.getFilesDir() + "/images");
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu không tồn tại
        }

        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(directory, fileName);

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("RecordActivity", "Error occurred", e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("RecordActivity", "Error occurred", e);
            }
        }

        return "images/" + fileName;
    }
}