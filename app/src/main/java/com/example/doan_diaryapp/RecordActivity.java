package com.example.doan_diaryapp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.ImageRecordAdapter;
import com.example.doan_diaryapp.Decorator.GridSpacingItemDecoration;

import java.util.Arrays;
import java.util.List;

public class RecordActivity extends BaseActivity {

    private List<Integer> imageMoodList = Arrays.asList(
            R.drawable.emoji_mood_joyful, R.drawable.emoji_mood_cool, R.drawable.emoji_mood_melting, R.drawable.emoji_mood_pleased,
            R.drawable.emoji_mood_happy, R.drawable.emoji_mood_surprise, R.drawable.emoji_mood_embarrassed, R.drawable.emoji_mood_normal,
            R.drawable.emoji_mood_fearful, R.drawable.emoji_mood_tired, R.drawable.emoji_mood_worried, R.drawable.emoji_mood_sad,
            R.drawable.emoji_mood_tired, R.drawable.emoji_mood_sleepy, R.drawable.emoji_mood_sick, R.drawable.emoji_mood_bored,
            R.drawable.emoji_mood_annoyed, R.drawable.emoji_mood_angry
    );

    private List<Integer> imageActivityList = Arrays.asList(
            R.drawable.emoji_activity_work, R.drawable.emoji_activity_study, R.drawable.emoji_activity_bake, R.drawable.emoji_activity_write,
            R.drawable.emoji_activity_sport, R.drawable.emoji_activity_gym, R.drawable.emoji_activity_watch_movie, R.drawable.emoji_activity_game,
            R.drawable.emoji_activity_play_instruments, R.drawable.emoji_activity_sing, R.drawable.emoji_activity_listen_to_music, R.drawable.emoji_activity_shopping,
            R.drawable.emoji_activity_paint, R.drawable.emoji_activity_party, R.drawable.emoji_activity_photograph, R.drawable.emoji_activity_sleep,
            R.drawable.emoji_activity_play_cards, R.drawable.emoji_activity_cook, R.drawable.emoji_activity_housework, R.drawable.emoji_activity_read
    );

    private List<Integer> imageCompanionList = Arrays.asList(
            R.drawable.emoji_companion_partner, R.drawable.emoji_companion_friends, R.drawable.emoji_companion_family, R.drawable.emoji_companion_pets
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter1 = new ImageRecordAdapter(imageMoodList);
        recyclerView1.setAdapter(adapter1);
        recyclerView1.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter2 = new ImageRecordAdapter(imageActivityList);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 4));
        ImageRecordAdapter adapter3 = new ImageRecordAdapter(imageCompanionList);
        recyclerView3.setAdapter(adapter3);
        recyclerView3.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));
    }
}