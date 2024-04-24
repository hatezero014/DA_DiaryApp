package com.example.doan_diaryapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.databinding.ActivityYourImagesInAppBinding;
import com.example.doan_diaryapp.ui.collection.CarouselAdapter;
import com.example.doan_diaryapp.ui.collection.CarouselModel;
import com.example.doan_diaryapp.ui.collection.ImageModel;
import com.example.doan_diaryapp.ui.collection.YourImagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class YourImagesInApp extends AppCompatActivity {

    private ActivityYourImagesInAppBinding binding;
    private YourImagesAdapter yourImagesAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_images_in_app);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_YourImage));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.carousel_recycler_view1);
        //yourImagesAdapter = new CarouselAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        yourImagesAdapter = new YourImagesAdapter(getListData(), this);

        recyclerView.setAdapter(yourImagesAdapter);

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
