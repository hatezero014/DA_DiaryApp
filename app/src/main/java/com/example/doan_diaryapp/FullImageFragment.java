// FullImageFragment.java
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
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FullImageFragment extends Fragment {

    private PhotoView imageView;
    private ImageView imageButton;
    private TextView tvDetails;
    private String file;
    private String strDetails;
    private String titleShare;
    private EntryPhotoService entryPhotoService;
    private EntryService entryService;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private FloatingActionButton floatingActionButtonAdd, floatingActionButtonDetails, floatingActionButtonShare;
    private boolean clicked = false;

    public static FullImageFragment newInstance(String file) {
        FullImageFragment fragment = new FullImageFragment();
        Bundle args = new Bundle();
        args.putString("image", file);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_image, container, false);

        imageView = view.findViewById(R.id.fullImageView);
        tvDetails = view.findViewById(R.id.tv_details);

        floatingActionButtonAdd = view.findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonDetails = view.findViewById(R.id.floatingActionButtonDetails);
        floatingActionButtonShare = view.findViewById(R.id.floatingActionButtonShare);

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_botton_animation);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_botton_animation);

        if (getArguments() != null) {
            file = getArguments().getString("image");
        }

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonAddClick();
            }
        });


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

        imageView.setImageURI(Uri.fromFile(new File(file)));

        entryPhotoService = new EntryPhotoService(getContext());
        entryService = new EntryService(getContext());

        Entry entry = getLisEntry(file);
        String title = getResources().getString(R.string.fgm_title) + " " + entry.getTitle();
        String note = getResources().getString(R.string.fgm_note) + " " + entry.getNote();
        String overall = getResources().getString(R.string.fgm_overall_score) + " " + entry.getOverallScore();
        String timeAndDay = entry.getDate();
        String[] days = timeAndDay.split(" ");
        String time = days[0];
        String day = days[1];
        timeAndDay = getResources().getString(R.string.fgm_Time) + " " + time + " " + getResources().getString(R.string.fgm_Day) + " " + day;
        strDetails = title + "\n" + note + "\n" + overall + "\n" + timeAndDay;
        titleShare = "[" + entry.getTitle() + "]" + "\n" + entry.getNote();

        //tvDetails.setText(strDetails);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("titleShare", titleShare);
        editor.putString("file", file);
        editor.putString("strDetails", strDetails);
        editor.apply();

        return view;
    }

    private Entry getLisEntry(String imagePath) {
        String date = entryPhotoService.getDate(imagePath);
        return entryService.FindByDate(new Entry(), date);
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
        File folder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try{
            folder.mkdir();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(getContext(), "com.example.doan_diaryapp", file );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    private void openDetailDiaLog(int gravity) {
        final Dialog dialog = new Dialog(getContext());
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


    private void showDaily(String imagePath) {
        Intent intent = new Intent(getContext(), RecordActivity.class);
        entryPhotoService = new EntryPhotoService(getContext());
        String date = entryPhotoService.getDate(imagePath);
        intent.putExtra("Date", date);
        this.startActivity(intent);
    }


}
