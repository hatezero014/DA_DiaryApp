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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_app);
        recyclerView = findViewById(R.id.recyclerView);
        AddData();
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Lấy giờ (24h)
        int minute = calendar.get(Calendar.MINUTE); // Lấy phút
        int day = calendar.get(Calendar.DAY_OF_MONTH); // Lấy ngày
        int month = calendar.get(Calendar.MONTH) + 1; // Lấy tháng (phải cộng thêm 1 vì tháng trong Calendar bắt đầu từ 0)
        int year = calendar.get(Calendar.YEAR); // Lấy năm
        CurrentTime = hour + ":" + minute + " " + day + "/" + month + "/" + year;
        // In ra màn hình để kiểm tra
        Log.d("CurrentTime", "Giờ: " + hour + ", Phút: " + minute + ", Ngày: " + day + ", Tháng: " + month + ", Năm: " + year);
        NotificationService notificationService = new NotificationService(this);
        notificationService.Add(new Notification(CurrentTime, this.AddNotification));

    }

    private void LoadNotificationbyDatabase()
    {
        NotificationService notificationService = new NotificationService(this);
        ArrayList<Notification> notifications = notificationService.GetAll(Notification.class);
//        List<Notification> notifications = new ArrayList<>();
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
//        notifications.add( new Notification("1", "Minh"));
        if(notifications != null) {
            notificationAdapter = new NotificationAdapter(notifications);
            recyclerView.setAdapter(notificationAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
    }
}