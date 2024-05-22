package com.example.doan_diaryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Files.ImageFinder;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.databinding.ActivityYourImagesInAppBinding;
import com.example.doan_diaryapp.ui.collection.CarouselAdapter;
import com.example.doan_diaryapp.ui.collection.CarouselModel;
import com.example.doan_diaryapp.ui.collection.ImageModel;
import com.example.doan_diaryapp.ui.collection.YourImagesAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YourImagesInApp extends AppCompatActivity {

    private ActivityYourImagesInAppBinding binding;
    private YourImagesAdapter yourImagesAdapter;
    private RecyclerView recyclerView;

    File imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_images_in_app);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        AppBarLayout appBarLayout = findViewById(R.id.ImageTopBar);
        MaterialToolbar toolbar = (MaterialToolbar) appBarLayout.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.carousel_recycler_view1);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        yourImagesAdapter = new YourImagesAdapter(getListData(), this);

        recyclerView.setAdapter(yourImagesAdapter);

        String directoryPath = "/data/user/0/com.example.doan_diaryapp/files/";
        File myImageFile = ImageFinder.findImage(directoryPath);

        yourImagesAdapter.setOnImageClickListener(new YourImagesAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                CarouselModel clickedModel = getListData().get(position);
                String imagePath = clickedModel.getImagePath();

                Intent intent = new Intent(YourImagesInApp.this, FullImageView.class);
                intent.putExtra("pos", position);
                intent.putExtra("image", imagePath);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<CarouselModel> getListData()
    {
        EntryPhotoService entryPhotoService = new EntryPhotoService(this);
        ArrayList<CarouselModel> entryPhotos = entryPhotoService.getPhotoFromDatabase();
        return entryPhotos;
    }



}
