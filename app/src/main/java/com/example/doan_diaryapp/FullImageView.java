package com.example.doan_diaryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class FullImageView extends AppCompatActivity {

    EntryPhotoService entryPhotoService;
    int position;
    PhotoView imageView;
    ImageButton imageButton;

    String strDetails;

    TextView tvDetails;
    Context context;
    String file;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private FloatingActionButton floatingActionButtonAdd, floatingActionButtonDetails, floatingActionButtonShare;

    private boolean clicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_full_image_view);

        imageView = findViewById(R.id.fullImageView);
        imageButton = findViewById(R.id.imageButton);
//        imgDetail = findViewById(R.id.imageViewDetails);
//        imgShare = findViewById(R.id.imageViewShare);
//        tvDetails = findViewById(R.id.tv_details);
        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonDetails = findViewById(R.id.floatingActionButtonDetails);
        floatingActionButtonShare = findViewById(R.id.floatingActionButtonShare);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_botton_animation);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_botton_animation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            position = bundle.getInt("pos");

        }
        Intent intent = getIntent();
        if(intent!=null){
            file = intent.getStringExtra("image");
        }
        imageView.setImageURI(Uri.fromFile(new File(file)));


        Entry entry = getLisEntry(file);
        String title = getResources().getString(R.string.fgm_title) + " " + entry.getTitle();
        String note = getResources().getString(R.string.fgm_note) + " " +entry.getNote();
        String Overrall = getResources().getString(R.string.fgm_overall_score) + " " + entry.getOverallScore();
        String timeandday = entry.getDate();
        String [] days = timeandday.split(" ");
        String time = days[0];
        String day = days[1];
        timeandday = getResources().getString(R.string.fgm_Time)+ " " + time + " " + getResources().getString(R.string.fgm_Day)+ " " + day;
        strDetails = title + "\n" + note + "\n" + Overrall + "\n" + timeandday;


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonAddClick();
            }
        });


        floatingActionButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void showDaily(String imagePath) {
        Intent intent = new Intent(this, RecordActivity.class);
        entryPhotoService = new EntryPhotoService(this);
        String date = entryPhotoService.getDate(imagePath);
        intent.putExtra("Date", date);
        this.startActivity(intent);
    }

    public Entry getLisEntry(String imagePath){
        EntryService entryService = new EntryService(this);
        entryPhotoService = new EntryPhotoService(this);
        String date = entryPhotoService.getDate(imagePath);
        Entry entry = entryService.FindByDate(new Entry(), date);
        return entry;

    }
}