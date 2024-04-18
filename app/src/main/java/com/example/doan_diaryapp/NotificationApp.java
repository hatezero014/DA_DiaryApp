package com.example.doan_diaryapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.NotificationAdapter;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class NotificationApp extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_app);


        NotificationService notificationService = new NotificationService(this);
        ArrayList<Notification> notifications = notificationService.GetAll(Notification.class);
//        List<Notification> notifications = new ArrayList<>();
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
        recyclerView = findViewById(R.id.recyclerView);
        NotificationAdapter notificationAdapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
}