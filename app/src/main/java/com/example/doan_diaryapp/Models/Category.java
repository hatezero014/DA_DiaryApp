package com.example.doan_diaryapp.Models;

import java.util.List;

public class Category {
    String nameCategory;
    List<Notification> notificationList;

    public Category(String nameCategory, List<Notification> notificationList) {
        this.nameCategory = nameCategory;
        this.notificationList = notificationList;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
