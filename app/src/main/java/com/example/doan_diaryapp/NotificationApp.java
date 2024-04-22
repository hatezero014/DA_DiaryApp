package com.example.doan_diaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.NotificationAdapter;
import com.example.doan_diaryapp.Controllers.DataHolderController;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationApp extends BaseActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_app);
        recyclerView = findViewById(R.id.recyclerView);

        LoadNotificationbyDatabase();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void LoadNotificationbyDatabase()
    {
        NotificationService notificationService = new NotificationService(this);
        ArrayList<Notification> notifications = notificationService.GetAllOrderByDESC(Notification.class, "Id DESC");
        if(notifications != null) {
            notificationAdapter = new NotificationAdapter(notifications);
            recyclerView.setAdapter(notificationAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
    }
}