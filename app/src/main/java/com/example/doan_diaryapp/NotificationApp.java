package com.example.doan_diaryapp;

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
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationApp extends BaseActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;

    String AddNotification;

    String CurrentTime;

    public NotificationApp(){}

    public NotificationApp(String addNotification)
    {

        this.AddNotification = addNotification;
        AddData();
    }
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

    private void AddData()
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        CurrentTime = hour + ":" + minute + " " + day + "/" + month + "/" + year;
        Log.d("CurrentTime", "Giờ: " + hour + ", Phút: " + minute + ", Ngày: " + day + ", Tháng: " + month + ", Năm: " + year);
        NotificationService notificationService = new NotificationService(this);
        notificationService.Add(new Notification(CurrentTime, this.AddNotification));

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