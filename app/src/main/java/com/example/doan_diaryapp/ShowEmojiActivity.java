package com.example.doan_diaryapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.EmojiAdapter;
import com.example.doan_diaryapp.Models.EmojiInfo;

import java.util.ArrayList;
import java.util.List;

public class ShowEmojiActivity extends AppCompatActivity {
    private EmojiAdapter emojiAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_emoji);

        recyclerView = findViewById(R.id.rcv_emoji);

        emojiAdapter = new EmojiAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);

        emojiAdapter.setData(getListEmoji());
        recyclerView.setAdapter(emojiAdapter);
    }

    private List<EmojiInfo> getListEmoji() {
        List<EmojiInfo> list = new ArrayList<>();

        ArrayList<String> dataList = getIntent().getStringArrayListExtra("sortedData");
        if (dataList != null) {
            for (String item : dataList) {
                String[] parts = item.split(",");
                if (parts.length == 2) {
                    list.add(new EmojiInfo(parts[0], parts[1]));
                }
            }
        }

        return list;
    }
}