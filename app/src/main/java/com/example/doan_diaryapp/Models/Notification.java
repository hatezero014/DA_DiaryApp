package com.example.doan_diaryapp.Models;

public class Notification {
    int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    String notificationContent;

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public Notification(){}

    public Notification(String notificationContent, String time)
    {
        this.notificationContent = notificationContent;
        this.time = time;
    }
}
