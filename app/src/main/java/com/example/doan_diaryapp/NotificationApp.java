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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.CategoryAdapter;
import com.example.doan_diaryapp.Adapter.NotificationAdapter;
import com.example.doan_diaryapp.Controllers.DataHolderController;
import com.example.doan_diaryapp.Models.Category;
import com.example.doan_diaryapp.Models.DayDistinct;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.Service.NotificationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationApp extends BaseActivity {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_app);
//
//        String chuocantruyen = "Tao nè";
//        NotificationService notificationService = new NotificationService(this);
//        notificationService.Add(new Notification(getCurrentTime(),getCurrentDay(),chuocantruyen));

        recyclerView = findViewById(R.id.recyclerView);
        categoryAdapter = new CategoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        recyclerView.setAdapter(categoryAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private List<Category> getListCategory() {
        NotificationService notificationService = new NotificationService(this);;
        List<Category> categoryList = new ArrayList<>();
        List<DayDistinct> days = notificationService.DayDistinct(DayDistinct.class);
        for(DayDistinct day : days){
            String temp = day.getDay();
            String whereClause = "Day = ?";
            String[] whereArgs = new String[]{temp};
            ArrayList<Notification> notifications = notificationService.GetAllOrderByDESC(Notification.class, "Id desc", whereClause, whereArgs);
            categoryList.add(new Category(temp, notifications));


        }
        return categoryList;
    }
}