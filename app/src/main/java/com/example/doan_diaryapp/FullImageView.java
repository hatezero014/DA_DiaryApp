// FullImageView.java
package com.example.doan_diaryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.NotificationService;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FullImageView extends BaseActivity implements FullImageFragment.OnPageChangeListener {

    private EntryPhotoService entryPhotoService;
    PhotoView imageView;
    private ViewPager2 viewPager;
    private FullImagePagerAdapter adapter;
    private ArrayList<String> imagePaths;
    private int position;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private FloatingActionButton floatingActionButtonAdd, floatingActionButtonDetails, floatingActionButtonShare;
    private boolean clicked = false;
    String strDetails, titleShare, file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_image_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("pos");
            imagePaths = bundle.getStringArrayList("images");
        }
        imageView = findViewById(R.id.fullImageView2);

        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd1);
        floatingActionButtonDetails = findViewById(R.id.floatingActionButtonDetails1);
        floatingActionButtonShare = findViewById(R.id.floatingActionButtonShare1);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_botton_animation);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_botton_animation);

        viewPager = findViewById(R.id.view_pager);
        adapter = new FullImagePagerAdapter(this, imagePaths);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position, false);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                FullImageFragment currentFragment = (FullImageFragment) getSupportFragmentManager()
                        .findFragmentByTag("f" + viewPager.getCurrentItem());
                if (currentFragment != null) {
                    currentFragment.notifyPageScrollStateChanged(state);
                }
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    floatingActionButtonAdd.setVisibility(View.GONE);
                } else if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                    floatingActionButtonAdd.
                            setVisibility(View.VISIBLE);
                }
            }
        });

//        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onAddButtonAddClick();
//            }
//        });


        floatingActionButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(file);
            }
        });

        floatingActionButtonShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        floatingActionButtonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailDiaLog(Gravity.CENTER);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        titleShare = sharedPreferences.getString("titleShare", "");
        file = sharedPreferences.getString("file", "");
        strDetails = sharedPreferences.getString("strDetails", "");
        imageView.setImageURI(Uri.fromFile(new File(file)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void shareImage(String file) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        shareImageAndText(bitmap);
    }

    private void shareImageAndText(Bitmap bitmap) {

        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, titleShare);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Image Sub");
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(), "images");
        Uri uri = null;
        try{
            folder.mkdir();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.example.doan_diaryapp", file );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    private void openDetailDiaLog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_detail_image);

        Window window = dialog.getWindow();
        if(window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttibutes = window.getAttributes();
        windowAttibutes.gravity = gravity;
        window.setAttributes(windowAttibutes);

//        NotificationService notificationService = new NotificationService(this);
//        notificationService.Add(new Notification(getCurrentTime(), getCurrentDay(), 3, "5;7.77"));

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(false);
        }
        Button btnOK = dialog.findViewById(R.id.btnCancel1);
        Button btnGotoDiary = dialog.findViewById(R.id.btn_Go_to_Diary);
        TextView textView = dialog.findViewById(R.id.tv_detail1);
        textView.setText(strDetails);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnGotoDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDaily(file);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onAddButtonAddClick() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setAnimation(boolean clicked) {
        if(!clicked){

            floatingActionButtonShare.setVisibility(View.VISIBLE);
            floatingActionButtonDetails.setVisibility(View.VISIBLE);
        }
        else{

            floatingActionButtonShare.setVisibility(View.INVISIBLE);
            floatingActionButtonDetails.setVisibility(View.INVISIBLE);
        }
    }

    private void setVisibility(boolean clicked) {
        if(!clicked){
            floatingActionButtonShare.setAnimation(fromBottom);
            floatingActionButtonDetails.setAnimation(fromBottom);
            floatingActionButtonAdd.setAnimation(rotateOpen);
        }
        else{
            floatingActionButtonShare.setAnimation(toBottom);
            floatingActionButtonDetails.setAnimation(toBottom);
            floatingActionButtonAdd.setAnimation(rotateClose);
        }
    }

    private void showDaily(String imagePath) {
        Intent intent = new Intent(this, RecordActivity.class);
        entryPhotoService = new EntryPhotoService(this);
        String date = entryPhotoService.getDate(imagePath);
        intent.putExtra("Date", date);
        this.startActivity(intent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
