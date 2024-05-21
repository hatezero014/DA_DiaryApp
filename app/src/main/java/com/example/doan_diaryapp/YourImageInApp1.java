package com.example.doan_diaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.ui.collection.CarouselModel;
import com.example.doan_diaryapp.ui.collection.YourImagesAdapter;

import java.util.ArrayList;

public class YourImageInApp1 extends BaseActivity {

    private YourImagesAdapter yourImagesAdapter;
    private RecyclerView recyclerView;
    private ArrayList<CarouselModel> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_image_in_app1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.carousel_recycler_view111);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        imageList = getListData();
        yourImagesAdapter = new YourImagesAdapter(imageList, this);

        recyclerView.setAdapter(yourImagesAdapter);

        yourImagesAdapter.setOnImageClickListener(position -> {
            Intent intent = new Intent(this, FullImageView1.class);
            ArrayList<String> imagePaths = new ArrayList<>();
            for (CarouselModel model : imageList) {
                imagePaths.add(model.getImagePath());
            }
            intent.putExtra("pos", position);
            intent.putStringArrayListExtra("images", imagePaths);
            startActivity(intent);
        });

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

    private ArrayList<CarouselModel> getListData() {
        EntryPhotoService entryPhotoService = new EntryPhotoService(this);
        ArrayList<CarouselModel> entryPhotos = entryPhotoService.getPhotoFromDatabase();
        return entryPhotos;
    }
}