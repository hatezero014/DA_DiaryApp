package com.example.doan_diaryapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.databinding.ActivityYourImagesInAppBinding;
import com.example.doan_diaryapp.ui.collection.CarouselModel;
import com.example.doan_diaryapp.ui.collection.YourImagesAdapter;

import java.util.ArrayList;

public class YourImagesInApp extends AppCompatActivity {

    private ActivityYourImagesInAppBinding binding;
    private YourImagesAdapter yourImagesAdapter;
    private RecyclerView recyclerView;
    private EntryPhotoService entryPhotoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYourImagesInAppBinding.inflate(getLayoutInflater());


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_images_in_app);
        recyclerView = binding.carouselRecyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        entryPhotoService = new EntryPhotoService(this);
       ArrayList<CarouselModel> list = entryPhotoService.getPhotoFromDatabase();

        yourImagesAdapter = new YourImagesAdapter(list, this);
        recyclerView.setAdapter(yourImagesAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
