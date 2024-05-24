package com.example.doan_diaryapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.doan_diaryapp.Models.Notification;
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
import com.example.doan_diaryapp.Service.NotificationService;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordActivity extends BaseActivity {

    private OnBackPressedDispatcher dispatcher;

    private List<Integer> imageMoodList = new ArrayList<>();

    private List<Integer> imageActivityList = new ArrayList<>();

    private List<Integer> imageCompanionList = new ArrayList<>();

    private List<Integer> imageWeatherList = new ArrayList<>();

    private ImageButton btnDeImgFi, btnDeImgSe, btnDeImgTh;
    private ImageView imgFirst, imgSecond, imgThird;
    int countImage = 0;
    Boolean isCheckFavorite = false, checkBeforeDone = false;
    Uri imgFiUri = null, imgSeUri = null, imgThUri = null;
    String date;
    private static final int PICK_IMAGES_REQUEST = 1;
    Slider slider;
    TextView textNote, textCount, textTitle, textExistedImg;
    FrameLayout frameFirst, frameSecond, frameThird;
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

    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    String language;

    private SharedPreferences sharedPreferences1;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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
        createNotificationChannel();
        sharedPreferences1 = getSharedPreferences("currrentTime", MODE_PRIVATE);

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

        List<Integer> emotionGetAllIndex;
        List<String> emotionGetAllDesc;
        List<Integer> emotionGetAllIsActive;
        List<String> emotionGetAllIcon;
        List<Integer> activityGetAllIndex;
        List<String> activityGetAllDesc;
        List<Integer> activityGetAllIsActive;
        List<String> activityGetAllIcon;
        List<Integer> partnerGetAllIndex;
        List<Integer> partnerGetAllIsActive;
        List<String> partnerGetAllDesc;
        List<String> partnerGetAllIcon;
        List<Integer> weatherGetAllIndex;
        List<String> weatherGetAllDesc;
        List<Integer> weatherGetAllIsActive;
        List<String> weatherGetAllIcon;
        List<String> descMoodList, descActivityList, descPartnerList, descWeatherList;

        imgFirst = findViewById(R.id.imageFirst);
        imgSecond = findViewById(R.id.imageSecond);
        imgThird = findViewById(R.id.imageThird);
        btnDeImgFi = findViewById(R.id.btnDeImgFi);
        btnDeImgSe = findViewById(R.id.btnDeImgSe);
        btnDeImgTh = findViewById(R.id.btnDeImgTh);
        btnDone = findViewById(R.id.btnDone);
        textNote = findViewById(R.id.textNote);
        textTitle = findViewById(R.id.textTitle);
        slider = findViewById(R.id.slider);
        textCount = findViewById(R.id.txtCountImage);
        frameFirst = findViewById(R.id.frameImageFirst);
        frameSecond = findViewById(R.id.frameImageSecond);
        frameThird = findViewById(R.id.frameImageThird);
        textExistedImg = findViewById(R.id.textExistedImg);
        textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 4));

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 4));

        recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView4.setLayoutManager(new GridLayoutManager(this, 4));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String dateNew = date.split(" ")[1];
            actionBar.setTitle(dateNew);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Settings", android.app.Activity.MODE_PRIVATE);
        language = prefs.getString("My_Language", "");

        emotionGetAllIndex = new ArrayList<>();
        emotionGetAllDesc = new ArrayList<>();
        emotionGetAllIsActive = new ArrayList<>();
        emotionGetAllIcon = new ArrayList<>();
        activityGetAllIndex = new ArrayList<>();
        activityGetAllDesc = new ArrayList<>();
        activityGetAllIcon = new ArrayList<>();
        activityGetAllIsActive = new ArrayList<>();
        partnerGetAllIndex = new ArrayList<>();
        partnerGetAllDesc = new ArrayList<>();
        partnerGetAllIsActive = new ArrayList<>();
        partnerGetAllIcon = new ArrayList<>();
        weatherGetAllIcon = new ArrayList<>();
        weatherGetAllIndex = new ArrayList<>();
        weatherGetAllDesc = new ArrayList<>();
        weatherGetAllIsActive = new ArrayList<>();
        List<Integer> idEmotionList = new ArrayList<>();
        List<Integer> idActivityList = new ArrayList<>();
        List<Integer> idPartnerList = new ArrayList<>();
        List<Integer> idWeatherList = new ArrayList<>();

        List<Emotion> emotionList = emotionService.GetAll(Emotion.class);
        for (Emotion emotion : emotionList) {
            if (emotion.getIcon().equals("add"))
                continue;
            emotionGetAllIcon.add(emotion.getIcon());
            int emotionIdR = getResources().getIdentifier(emotion.getIcon(), "drawable", getPackageName());
            emotionGetAllIndex.add(emotionIdR);
            String desc = emotion.getDescEn();
            if (language.equals("vi"))
                desc = emotion.getDescVi();
            emotionGetAllDesc.add(desc);
            if (emotion.getIsActive() == 0) {
                continue;
            }
            emotionGetAllIsActive.add(emotion.getId() - 1);
            imageMoodList.add(emotionIdR);
            idEmotionList.add(emotion.getId());
        }

        List<Activity> activityList = activityService.GetAll(Activity.class);
        for (Activity activity : activityList) {
            if (activity.getIcon().equals("add"))
                continue;
            activityGetAllIcon.add(activity.getIcon());
            int activityIdR = getResources().getIdentifier(activity.getIcon(), "drawable", getPackageName());
            activityGetAllIndex.add(activityIdR);
            String desc = activity.getDescEn();
            if (language.equals("vi"))
                desc = activity.getDescVi();
            activityGetAllDesc.add(desc);
            if (activity.getIsActive() == 0) {
                continue;
            }
            activityGetAllIsActive.add(activity.getId() - 1);
            imageActivityList.add(activityIdR);
            idActivityList.add(activity.getId());
        }

        List<Partner> partnerList = partnerService.GetAll(Partner.class);
        for (Partner partner : partnerList) {
            if (partner.getIcon().equals("add"))
                continue;
            partnerGetAllIcon.add(partner.getIcon());
            int partnerIdR = getResources().getIdentifier(partner.getIcon(), "drawable", getPackageName());
            partnerGetAllIndex.add(partnerIdR);
            String desc = partner.getDescEn();
            if (language.equals("vi"))
                desc = partner.getDescVi();
            partnerGetAllDesc.add(desc);
            if (partner.getIsActive() == 0) {
                continue;
            }
            partnerGetAllIsActive.add(partner.getId() - 1);
            imageCompanionList.add(partnerIdR);
            idPartnerList.add(partner.getId());
        }

        List<Weather> weatherList = emotionService.GetAll(Weather.class);
        for (Weather weather : weatherList) {
            if (weather.getIcon().equals("add"))
                continue;
            weatherGetAllIcon.add(weather.getIcon());
            int weatherIdR = getResources().getIdentifier(weather.getIcon(), "drawable", getPackageName());
            weatherGetAllIndex.add(weatherIdR);
            String desc = weather.getDescEn();
            if (language.equals("vi"))
                desc = weather.getDescVi();
            weatherGetAllDesc.add(desc);
            if (weather.getIsActive() == 0) {
                continue;
            }
            weatherGetAllIsActive.add(weather.getId() - 1);
            imageWeatherList.add(weatherIdR);
            idWeatherList.add(weather.getId());
        }

        imageMoodList.add(R.drawable.add);
        imageActivityList.add(R.drawable.add);
        imageCompanionList.add(R.drawable.add);
        imageWeatherList.add(R.drawable.add);
        idEmotionList.add(-1);
        idActivityList.add(-1);
        idPartnerList.add(-1);
        idWeatherList.add(-1);

        descMoodList = new ArrayList<>();
        for (Integer resourceId : imageMoodList) {
            String resourceName = getResourceName(resourceId);
            Emotion emotion = emotionService.FindByIcon(Emotion.class, resourceName);
            String desc = emotion.getDescEn();
            if (language.equals("vi"))
                desc = emotion.getDescVi();
            descMoodList.add(desc);
        }

        descActivityList = new ArrayList<>();
        for (Integer resourceId : imageActivityList) {
            String resourceName = getResourceName(resourceId);
            Activity activity = activityService.FindByIcon(Activity.class, resourceName);
            String desc = activity.getDescEn();
            if (language.equals("vi"))
                desc = activity.getDescVi();
            descActivityList.add(desc);
        }

        descPartnerList = new ArrayList<>();
        for (Integer resourceId : imageCompanionList) {
            String resourceName = getResourceName(resourceId);
            Partner partner = partnerService.FindByIcon(Partner.class, resourceName);
            String desc = partner.getDescEn();
            if (language.equals("vi"))
                desc = partner.getDescVi();
            descPartnerList.add(desc);
        }

        descWeatherList = new ArrayList<>();
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
            textNote.setText(result.getNote());
            textTitle.setText(result.getTitle());
            slider.setValue((float)result.getOverallScore());
            ArrayList<EntryActivity> entryActivities = entryActivityService.GetAllByEntryId(EntryActivity.class, result.getId());
            ArrayList<EntryEmotion> entryEmotions = entryEmotionService.GetAllByEntryId(EntryEmotion.class, result.getId());
            ArrayList<EntryPartner> entryPartners = entryPartnerService.GetAllByEntryId(EntryPartner.class, result.getId());
            ArrayList<EntryWeather> entryWeathers = entryWeatherService.GetAllByEntryId(EntryWeather.class, result.getId());
            ArrayList<EntryPhoto> entryPhotos = entryPhotoService.GetAllByEntryId(EntryPhoto.class, result.getId());

            activityIndexes = new ArrayList<>();
            for (EntryActivity entity : entryActivities) {
                Activity activity = activityService.FindById(Activity.class, entity.getActivityId());
                activityIndexes.add(activity.getId());
            }

            emotionIndexes = new ArrayList<>();
            for (EntryEmotion entity : entryEmotions) {
                Emotion emotion = emotionService.FindById(Emotion.class, entity.getEmotionId());
                emotionIndexes.add(emotion.getId());
            }

            partnerIndexes = new ArrayList<>();
            for (EntryPartner entity : entryPartners) {
                Partner partner = partnerService.FindById(Partner.class, entity.getPartnerId());
                partnerIndexes.add(partner.getId());
            }

            weatherIndexes = new ArrayList<>();
            for (EntryWeather entity : entryWeathers) {
                Weather weather = weatherService.FindById(Weather.class, entity.getWeatherId());
                weatherIndexes.add(weather.getId());
            }

            adapter1 = new ImageRecordAdapter("Emotion", language, idEmotionList, imageMoodList, descMoodList, emotionIndexes, emotionGetAllIndex, emotionGetAllDesc, emotionGetAllIsActive, emotionGetAllIcon, this, this);
            recyclerView1.setAdapter(adapter1);
            recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter2 = new ImageRecordAdapter("Activity", language, idActivityList, imageActivityList, descActivityList, activityIndexes, activityGetAllIndex, activityGetAllDesc, activityGetAllIsActive, activityGetAllIcon, this, this);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter3 = new ImageRecordAdapter("Partner", language, idPartnerList, imageCompanionList, descPartnerList, partnerIndexes, partnerGetAllIndex, partnerGetAllDesc, partnerGetAllIsActive, partnerGetAllIcon, this, this);
            recyclerView3.setAdapter(adapter3);
            recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter4 = new ImageRecordAdapter("Weather", language, idWeatherList, imageWeatherList, descWeatherList, weatherIndexes, weatherGetAllIndex, weatherGetAllDesc, weatherGetAllIsActive, weatherGetAllIcon, this, this);
            recyclerView4.setAdapter(adapter4);
            recyclerView4.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

            for (int i = 0; i < entryPhotos.size(); i++) {
                EntryPhoto entryPhoto = entryPhotos.get(i);
                String relativePath = entryPhoto.getPhoto();
                File file = new File(this.getFilesDir(), relativePath);
                String absolutePath = file.getAbsolutePath();

                if (i == 0) {
                    Picasso.get().load(new File(absolutePath)).into(imgFirst);
                    imgFirst.setVisibility(View.VISIBLE);
                    frameFirst.setVisibility(View.VISIBLE);
                    textExistedImg.setVisibility(View.GONE);
                    countImage++;
                    btnDeImgFi.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    Picasso.get().load(new File(absolutePath)).into(imgSecond);
                    imgSecond.setVisibility(View.VISIBLE);
                    frameSecond.setVisibility(View.VISIBLE);
                    countImage++;
                    btnDeImgSe.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    Picasso.get().load(new File(absolutePath)).into(imgThird);
                    imgThird.setVisibility(View.VISIBLE);
                    frameThird.setVisibility(View.VISIBLE);
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
        else
        {
            adapter1 = new ImageRecordAdapter("Emotion", language, idEmotionList, imageMoodList, descMoodList, null, emotionGetAllIndex, emotionGetAllDesc, emotionGetAllIsActive, emotionGetAllIcon, this, this);
            recyclerView1.setAdapter(adapter1);
            recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter2 = new ImageRecordAdapter("Activity", language, idActivityList, imageActivityList, descActivityList, null, activityGetAllIndex, activityGetAllDesc, activityGetAllIsActive, activityGetAllIcon, this, this);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter3 = new ImageRecordAdapter("Partner", language, idPartnerList, imageCompanionList, descPartnerList, null, partnerGetAllIndex, partnerGetAllDesc, partnerGetAllIsActive, partnerGetAllIcon, this, this);
            recyclerView3.setAdapter(adapter3);
            recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
            adapter4 = new ImageRecordAdapter("Weather", language, idWeatherList, imageWeatherList, descWeatherList, null, weatherGetAllIndex, weatherGetAllDesc, weatherGetAllIsActive, weatherGetAllIcon, this, this);
            recyclerView4.setAdapter(adapter4);
            recyclerView4.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
        }
        checkBeforeDone = isCheckFavorite;
        btnDeImgFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFirst.setImageDrawable(null);
                btnDeImgFi.setVisibility(View.GONE);
                imgFiUri = null;
                countImage--;
                if (countImage == 0) {
                    textExistedImg.setVisibility(View.VISIBLE);
                }
                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                checkChangedImage = true;
                frameFirst.setVisibility(View.GONE);

                if (imgSecond.getDrawable() != null) {
                    imgFirst.setImageDrawable(imgSecond.getDrawable());
                    imgSecond.setImageDrawable(null);
                    btnDeImgFi.setVisibility(View.VISIBLE);
                    btnDeImgSe.setVisibility(View.GONE);
                    imgSecond.setVisibility(View.GONE);
                    imgFiUri = imgSeUri;
                    imgSeUri = null;
                    frameFirst.setVisibility(View.VISIBLE);
                    frameSecond.setVisibility(View.GONE);
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
                    frameSecond.setVisibility(View.VISIBLE);
                    frameThird.setVisibility(View.GONE);
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
                countImage--;
                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                checkChangedImage = true;
                frameSecond.setVisibility(View.GONE);

                if (imgThird.getDrawable() != null) {
                    imgSecond.setImageDrawable(imgThird.getDrawable());
                    imgSecond.setVisibility(View.VISIBLE);
                    imgThird.setImageDrawable(null);
                    btnDeImgSe.setVisibility(View.VISIBLE);
                    btnDeImgTh.setVisibility(View.GONE);
                    imgThird.setVisibility(View.GONE);
                    imgSeUri = imgThUri;
                    imgThUri = null;
                    frameSecond.setVisibility(View.VISIBLE);
                    frameThird.setVisibility(View.GONE);
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
                frameThird.setVisibility(View.GONE);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String notes = textNote.getText().toString();
                    String title = textTitle.getText().toString();
                    String wakeUp = "6:30";
                    String sleep = "6:30";
                    int overallScore = (int) slider.getValue();
                    List<Integer> selectedItems1 = adapter1.getSelectedItems();
                    List<Integer> selectedItems2 = adapter2.getSelectedItems();
                    List<Integer> selectedItems3 = adapter3.getSelectedItems();
                    List<Integer> selectedItems4 = adapter4.getSelectedItems();
                    if (result == null) {
                        Entry entity = new Entry(title, notes, date, overallScore, wakeUp, sleep);
                        entryService.Add(entity);
                        if (isCheckFavorite) {
                            importantDayService.Add(new ImportantDay(date));
                        }

                        int id = entryService.FindByDate(new Entry(), date).getId();
                        for (Integer imageId : selectedItems1) {
                            entryEmotionService.Add(new EntryEmotion(id, imageId));
                        }

                        for (Integer imageId : selectedItems2) {
                            entryActivityService.Add(new EntryActivity(id, imageId));
                        }

                        for (Integer imageId : selectedItems3) {
                            entryPartnerService.Add(new EntryPartner(id, imageId));
                        }

                        for (Integer imageId : selectedItems4) {
                            entryWeatherService.Add(new EntryWeather(id, imageId));
                        }

                        if (imgFirst.getDrawable() != null) {
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
                        try {
                            if(isCheckFavorite) {
                                NotificationService notificationService = new NotificationService(RecordActivity.this);
                                notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 2, null, 1 ));
                            }
                            else {
                                NotificationService notificationService = new NotificationService(RecordActivity.this);
                                notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 1, null, 1));
                            }
                        }

                        catch (Exception e) {
                            Toast.makeText(RecordActivity.this, R.string.record_toast_fail, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        showTimePicker();
                    }
                    else {
                        int id = result.getId();
                        Entry entity = new Entry(title, notes, date, overallScore, wakeUp, sleep);
                        ImportantDay importantDay = importantDayService.FindByDate(new ImportantDay(),date);
                        entryService.UpdateById(entity, id);
                        entryPhotoService.DeleteByEntryId(EntryPhoto.class, id);
                        entryActivityService.DeleteByEntryId(EntryActivity.class, id);
                        entryEmotionService.DeleteByEntryId(EntryEmotion.class, id);
                        entryPartnerService.DeleteByEntryId(EntryPartner.class, id);
                        entryWeatherService.DeleteByEntryId(EntryWeather.class, id);
                        if (isCheckFavorite) {
                            if (importantDay == null) {
                                importantDayService.Add(new ImportantDay(date));
                            }
                        }
                        else {
                            if (importantDay != null) {
                                importantDayService.DeleteById(ImportantDay.class, importantDay.getId());
                            }
                        }

                        for (Integer imageId : selectedItems1) {
                            entryEmotionService.Add(new EntryEmotion(id, imageId));
                        }

                        for (Integer imageId : selectedItems2) {
                            entryActivityService.Add(new EntryActivity(id, imageId));
                        }

                        for (Integer imageId : selectedItems3) {
                            entryPartnerService.Add(new EntryPartner(id, imageId));
                        }

                        for (Integer imageId : selectedItems4) {
                            entryWeatherService.Add(new EntryWeather(id, imageId));
                        }

                        if (imgFirst.getDrawable() != null) {
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
                        try {
                            NotificationService notificationService = new NotificationService(RecordActivity.this);
                            notificationService.DeleteById(Notification.class, id);
                            if(isCheckFavorite) {
                                notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 8, null, 1 ));
                            }
                            else {
                                notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 7, null, 1));
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(RecordActivity.this, R.string.record_toast_fail, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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

        TextView toggleList1 = findViewById(R.id.list1);
        TextView toggleList2 = findViewById(R.id.list2);
        TextView toggleList3 = findViewById(R.id.list3);
        TextView toggleList4 = findViewById(R.id.list4);

        toggleList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleVisibility(recyclerView1)) {
                    toggleList1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);
                }
                else {
                    toggleList1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_more, 0);
                }
            }
        });

        toggleList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleVisibility(recyclerView2)) {
                    toggleList2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);
                }
                else {
                    toggleList2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_more, 0);
                }
            }
        });

        toggleList3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleVisibility(recyclerView3)) {
                    toggleList3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);
                }
                else {
                    toggleList3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_more, 0);
                }
            }
        });

        toggleList4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleVisibility(recyclerView4)) {
                    toggleList4.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);
                }
                else {
                    toggleList4.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_more, 0);
                }
            }
        });

        LinearLayout images = findViewById(R.id.linearImages);

        textCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleVisibility(images)) {
                    textCount.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);
                }
                else {
                    textCount.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.expand_more, 0);
                }
            }
        });
    }

    public void updateRecyclerView1() {
        List<Integer> idEmotionList = new ArrayList<>();
        List<Integer> emotionGetAllIndex;
        List<String> emotionGetAllDesc;
        List<Integer> emotionGetAllIsActive;
        List<String> emotionGetAllIcon;
        List<String> descMoodList;
        emotionGetAllIndex = new ArrayList<>();
        emotionGetAllDesc = new ArrayList<>();
        emotionGetAllIsActive = new ArrayList<>();
        emotionGetAllIcon = new ArrayList<>();
        List<Emotion> emotionList = emotionService.GetAll(Emotion.class);
        imageMoodList = new ArrayList<>();
        for (Emotion emotion : emotionList) {
            if (emotion.getIcon().equals("add"))
                continue;
            emotionGetAllIcon.add(emotion.getIcon());
            int emotionIdR = getResources().getIdentifier(emotion.getIcon(), "drawable", getPackageName());
            emotionGetAllIndex.add(emotionIdR);
            String desc = emotion.getDescEn();
            if (language.equals("vi"))
                desc = emotion.getDescVi();
            emotionGetAllDesc.add(desc);
            if (emotion.getIsActive() == 0) {
                continue;
            }
            emotionGetAllIsActive.add(emotion.getId() - 1);
            imageMoodList.add(emotionIdR);
            idEmotionList.add(emotion.getId());
        }
        imageMoodList.add(R.drawable.add);
        idEmotionList.add(-1);
        descMoodList = new ArrayList<>();
        for (Integer resourceId : imageMoodList) {
            String resourceName = getResourceName(resourceId);
            Emotion emotion = emotionService.FindByIcon(Emotion.class, resourceName);
            String desc = emotion.getDescEn();
            if (language.equals("vi"))
                desc = emotion.getDescVi();
            descMoodList.add(desc);
        }
        emotionIndexes = adapter1.getSelectedItems();
        adapter1 = new ImageRecordAdapter("Emotion", language, idEmotionList, imageMoodList, descMoodList, emotionIndexes, emotionGetAllIndex, emotionGetAllDesc, emotionGetAllIsActive, emotionGetAllIcon, this, this);
        recyclerView1.setAdapter(adapter1);
    }

    public void updateRecyclerView2() {
        List<Integer> idActivityList = new ArrayList<>();
        List<Integer> activityGetAllIndex;
        List<String> activityGetAllDesc;
        List<Integer> activityGetAllIsActive;
        List<String> activityGetAllIcon;
        activityGetAllIndex = new ArrayList<>();
        activityGetAllDesc = new ArrayList<>();
        activityGetAllIcon = new ArrayList<>();
        activityGetAllIsActive = new ArrayList<>();
        List<Activity> activityList = activityService.GetAll(Activity.class);
        imageActivityList = new ArrayList<>();
        for (Activity activity : activityList) {
            if (activity.getIcon().equals("add"))
                continue;
            activityGetAllIcon.add(activity.getIcon());
            int activityIdR = getResources().getIdentifier(activity.getIcon(), "drawable", getPackageName());
            activityGetAllIndex.add(activityIdR);
            String desc = activity.getDescEn();
            if (language.equals("vi"))
                desc = activity.getDescVi();
            activityGetAllDesc.add(desc);
            if (activity.getIsActive() == 0) {
                continue;
            }
            activityGetAllIsActive.add(activity.getId() - 1);
            imageActivityList.add(activityIdR);
            idActivityList.add(activity.getId());
        }
        imageActivityList.add(R.drawable.add);
        idActivityList.add(-1);
        List<String> descActivityList = new ArrayList<>();
        for (Integer resourceId : imageActivityList) {
            String resourceName = getResourceName(resourceId);
            Activity activity = activityService.FindByIcon(Activity.class, resourceName);
            String desc = activity.getDescEn();
            if (language.equals("vi"))
                desc = activity.getDescVi();
            descActivityList.add(desc);
        }
        activityIndexes = adapter2.getSelectedItems();
        adapter2 = new ImageRecordAdapter("Activity", language, idActivityList, imageActivityList, descActivityList, activityIndexes, activityGetAllIndex, activityGetAllDesc, activityGetAllIsActive, activityGetAllIcon, this, this);
        recyclerView2.setAdapter(adapter2);
    }

    public void updateRecyclerView3() {
        List<Integer> idPartnerList = new ArrayList<>();
        List<Integer> partnerGetAllIndex;
        List<Integer> partnerGetAllIsActive;
        List<String> partnerGetAllDesc;
        List<String> partnerGetAllIcon;
        partnerGetAllIndex = new ArrayList<>();
        partnerGetAllDesc = new ArrayList<>();
        partnerGetAllIsActive = new ArrayList<>();
        partnerGetAllIcon = new ArrayList<>();
        List<Partner> partnerList = partnerService.GetAll(Partner.class);
        imageCompanionList = new ArrayList<>();
        for (Partner partner : partnerList) {
            if (partner.getIcon().equals("add"))
                continue;
            partnerGetAllIcon.add(partner.getIcon());
            int partnerIdR = getResources().getIdentifier(partner.getIcon(), "drawable", getPackageName());
            partnerGetAllIndex.add(partnerIdR);
            String desc = partner.getDescEn();
            if (language.equals("vi"))
                desc = partner.getDescVi();
            partnerGetAllDesc.add(desc);
            if (partner.getIsActive() == 0) {
                continue;
            }
            partnerGetAllIsActive.add(partner.getId() - 1);
            imageCompanionList.add(partnerIdR);
            idPartnerList.add(partner.getId());
        }
        imageCompanionList.add(R.drawable.add);
        idPartnerList.add(-1);
        List<String> descPartnerList = new ArrayList<>();
        for (Integer resourceId : imageCompanionList) {
            String resourceName = getResourceName(resourceId);
            Partner partner = partnerService.FindByIcon(Partner.class, resourceName);
            String desc = partner.getDescEn();
            if (language.equals("vi"))
                desc = partner.getDescVi();
            descPartnerList.add(desc);
        }
        partnerIndexes = adapter3.getSelectedItems();
        adapter3 = new ImageRecordAdapter("Partner", language, idPartnerList, imageCompanionList, descPartnerList, partnerIndexes, partnerGetAllIndex, partnerGetAllDesc, partnerGetAllIsActive, partnerGetAllIcon, this, this);
        recyclerView3.setAdapter(adapter3);
    }

    public void updateRecyclerView4() {
        List<Integer> idWeatherList = new ArrayList<>();
        List<Integer> weatherGetAllIndex;
        List<String> weatherGetAllDesc;
        List<Integer> weatherGetAllIsActive;
        List<String> weatherGetAllIcon;
        weatherGetAllIcon = new ArrayList<>();
        weatherGetAllIndex = new ArrayList<>();
        weatherGetAllDesc = new ArrayList<>();
        weatherGetAllIsActive = new ArrayList<>();
        List<Weather> weatherList = emotionService.GetAll(Weather.class);
        imageWeatherList = new ArrayList<>();
        for (Weather weather : weatherList) {
            if (weather.getIcon().equals("add"))
                continue;
            weatherGetAllIcon.add(weather.getIcon());
            int weatherIdR = getResources().getIdentifier(weather.getIcon(), "drawable", getPackageName());
            weatherGetAllIndex.add(weatherIdR);
            String desc = weather.getDescEn();
            if (language.equals("vi"))
                desc = weather.getDescVi();
            weatherGetAllDesc.add(desc);
            if (weather.getIsActive() == 0) {
                continue;
            }
            weatherGetAllIsActive.add(weather.getId() - 1);
            imageWeatherList.add(weatherIdR);
            idWeatherList.add(weather.getId());
        }
        imageWeatherList.add(R.drawable.add);
        idWeatherList.add(-1);
        List<String> descWeatherList = new ArrayList<>();
        for (Integer resourceId : imageWeatherList) {
            String resourceName = getResourceName(resourceId);
            Weather weather = weatherService.FindByIcon(Weather.class, resourceName);
            String desc = weather.getDescEn();
            if (language.equals("vi"))
                desc = weather.getDescVi();
            descWeatherList.add(desc);
        }
        weatherIndexes = adapter4.getSelectedItems();
        adapter4 = new ImageRecordAdapter("Weather", language, idWeatherList, imageWeatherList, descWeatherList, weatherIndexes, weatherGetAllIndex, weatherGetAllDesc, weatherGetAllIsActive, weatherGetAllIcon, this, this);
        recyclerView4.setAdapter(adapter4);
    }


    private boolean toggleVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            return true;
        } else {
            view.setVisibility(View.GONE);
            return false;
        }
    }

    private String getResourceName(int resourceId) {
        return getResources().getResourceEntryName(resourceId);
    }

    void showSnackBar(String content) {
        Toast.makeText(RecordActivity.this, content, Toast.LENGTH_SHORT).show();
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
                        count = 3 - countImage;
                    }
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        if (imgFirst.getDrawable() == null) {
                            imgFirst.setImageURI(imageUri);
                            imgFirst.setVisibility(View.VISIBLE);
                            frameFirst.setVisibility(View.VISIBLE);
                            imgFiUri = imageUri;
                            btnDeImgFi.setVisibility(View.VISIBLE);
                            checkChangedImage = true;
                            countImage++;
                            textExistedImg.setVisibility(View.GONE);
                            textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                        }
                        else {
                            if (imgSecond.getDrawable() == null) {
                                imgSecond.setImageURI(imageUri);
                                imgSecond.setVisibility(View.VISIBLE);
                                frameSecond.setVisibility(View.VISIBLE);
                                imgSeUri = imageUri;
                                btnDeImgSe.setVisibility(View.VISIBLE);
                                checkChangedImage = true;
                                countImage++;;
                                textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
                            }
                            else {
                                if (imgThird.getDrawable() == null) {
                                    imgThird.setImageURI(imageUri);
                                    imgThird.setVisibility(View.VISIBLE);
                                    frameThird.setVisibility(View.VISIBLE);
                                    imgThUri = imageUri;
                                    checkChangedImage = true;
                                    btnDeImgTh.setVisibility(View.VISIBLE);
                                    countImage++;;
                                    textCount.setText(getResources().getString(R.string.record_title_add_image, countImage, 3));
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
                isCheckFavorite = true;
                item.setIcon(R.drawable.state_filled_record_star);
            } else {
                item.setIcon(R.drawable.state_outlined_record_star);
                isCheckFavorite = false;
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
        String notes = textNote.getText().toString();
        String title = textTitle.getText().toString();
        String wakeUp = "6:30";
        String sleep = "6:30";
        int overallScore = (int) slider.getValue();
        List<Integer> selectedItems1 = adapter1.getSelectedItems();
        List<Integer> selectedItems2 = adapter2.getSelectedItems();
        List<Integer> selectedItems3 = adapter3.getSelectedItems();
        List<Integer> selectedItems4 = adapter4.getSelectedItems();
        Entry entity = new Entry(title, notes, date, overallScore, wakeUp, sleep);
        if (result == null) {
            return !notes.isEmpty() || !title.isEmpty() || overallScore != 5
                    || !selectedItems1.isEmpty() || !selectedItems2.isEmpty()
                    || !selectedItems3.isEmpty() || !selectedItems4.isEmpty();
        }
        boolean checkChanged = false;
        if (result.getOverallScore() == entity.getOverallScore()
                && result.getTitle().equals(entity.getTitle())
                && result.getNote().equals(entity.getNote())
                && isCheckFavorite == checkBeforeDone) {
            int entryId = result.getId();
            ArrayList<EntryEmotion> entryEmotions = entryEmotionService.GetAllByEntryId(EntryEmotion.class, entryId);
            ArrayList<EntryActivity> entryActivities = entryActivityService.GetAllByEntryId(EntryActivity.class, entryId);
            ArrayList<EntryPartner> entryPartners = entryPartnerService.GetAllByEntryId(EntryPartner.class, entryId);
            ArrayList<EntryWeather> entryWeathers = entryWeatherService.GetAllByEntryId(EntryWeather.class, entryId);
            int countEmotion = 0, countActivity = 0, countPartner = 0, countWeather = 0;
            for (EntryEmotion entryEmotion : entryEmotions) {
                int id = entryEmotion .getEmotionId();
                if (emotionService.FindById(Emotion.class, id).getIsActive() == 1) {
                    countEmotion++;
                }
            }
            for (EntryActivity entryActivity : entryActivities) {
                int id = entryActivity .getActivityId();
                if (entryActivityService.FindById(Activity.class, id).getIsActive() == 1) {
                    countActivity++;
                }
            }
            for (EntryPartner entryPartner : entryPartners) {
                int id = entryPartner .getPartnerId();
                if (partnerService.FindById(Partner.class, id).getIsActive() == 1) {
                    countPartner++;
                }
            }
            for (EntryWeather entryWeather : entryWeathers) {
                int id = entryWeather .getWeatherId();
                if (weatherService.FindById(Weather.class, id).getIsActive() == 1) {
                    countWeather++;
                }
            }
            if (countEmotion == selectedItems1.size()
                    && countActivity == selectedItems2.size()
                    && countPartner == selectedItems3.size()
                    && countWeather == selectedItems4.size()) {
                for (Integer imageId : selectedItems1) {
                    Emotion emotion = emotionService.FindById(Emotion.class, imageId);
                    if (emotion.IsActive == 0)
                        continue;
                    if (entryEmotionService.FindByEntryIdAndEmotionId(EntryEmotion.class, entryId, emotion.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems2) {
                    Activity activity = activityService.FindById(Activity.class, imageId);
                    if (activity.IsActive == 0)
                        continue;
                    if (entryActivityService.FindByEntryIdAndActivityId(EntryActivity.class, entryId, activity.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems3) {
                    Partner partner = partnerService.FindById(Partner.class, imageId);
                    if (partner.IsActive == 0)
                        continue;
                    if (entryPartnerService.FindByEntryIdAndPartnerId(EntryPartner.class, entryId, partner.getId()) == null) {
                        checkChanged = true;
                        break;
                    }
                }

                for (Integer imageId : selectedItems4) {
                    Weather weather = weatherService.FindById(Weather.class, imageId);
                    if (weather.IsActive == 0)
                        continue;
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
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
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

    private final ActivityResultLauncher<Intent> requestExactAlarmPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        setAlarm();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void checkAndRequestExactAlarmPermission() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                setAlarm();
            } else {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                requestExactAlarmPermissionLauncher.launch(intent);
            }
        } else {
            setAlarm();
        }
    }

    private void setAlarm() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (calendar != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show();
        }
    }
    private void cancelAlarm() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void showTimePicker() {
        cancelAlarm();
        calendar = Calendar.getInstance();
//        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
//        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
//        int futureHour = currentHour;
//        int futureMinute = currentMinute + 2;
//        if(futureMinute>59){
//            futureMinute = futureMinute-60;
//            futureHour++;
//            if(futureHour>23){
//                futureHour=0;
//            }
//        }
        int futureYear = currentYear;
        int futureMonth = currentMonth;
        int futureDay = currentDay + 7;

        switch (currentMonth){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if(futureDay>31){
                    futureDay = futureDay -31;
                    futureMonth++;
                }
                if(futureMonth>12){
                    futureMonth = 1;
                    futureYear++;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if(futureDay>30){
                    futureDay = futureDay -30;
                    futureMonth++;
                }
                break;
            case 2:
                if((currentYear % 4 == 0 && currentYear % 100 !=0) || currentYear % 400 ==0)
                {
                    if(futureDay>29){
                        futureDay = futureDay -29;
                        futureMonth++;
                    }
                }
                else {
                    if(futureDay>28){
                        futureDay = futureDay -28;
                        futureMonth++;
                    }
                }
                break;
        }
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putInt("year", futureYear);
        editor.putInt("month", futureMonth);
        editor.putInt("day", futureDay);
        editor.apply();
        calendar.set(Calendar.YEAR, futureYear);
        calendar.set(Calendar.MONTH, futureMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, futureDay);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        checkAndRequestExactAlarmPermission();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.remind);
            String description = getString(R.string.remind_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.id_NotificationReceiver), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}