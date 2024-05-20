package com.example.doan_diaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Service.EntryService;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    String date;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        textView = findViewById(R.id.textView111);

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");


        Entry entry = getLisEntry();
        textView.setText(String.valueOf(entry.getId()) + String.valueOf(entry.getOverallScore()) + entry.getDate() + entry.getTitle());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public Entry getLisEntry(){
        EntryService entryService = new EntryService(this);
        Entry entry = entryService.FindByDate(new Entry(), date);
        return entry;

    }
}