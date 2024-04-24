package com.example.doan_diaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.frequently_recorded);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        emojiAdapter = new EmojiAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);

        emojiAdapter.setData(getListEmoji());
        recyclerView.setAdapter(emojiAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Sử dụng FragmentManager để trở về fragment trước
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(); // Trở về fragment trước đó
            } else {
                finish(); // Nếu không có fragment trong backstack, đóng activity
            }
            return true; // Đã xử lý sự kiện
        }
        return super.onOptionsItemSelected(item); // Chuyển xử lý tiếp tục lên
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