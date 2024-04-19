package com.example.doan_diaryapp.Models;

public class Notification {
    int Id;

    String Time;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNotificationContent() {
        return NotificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        NotificationContent = notificationContent;
    }

    public Notification() {
    }

    public Notification(String time, String notificationContent) {
        Time = time;
        NotificationContent = notificationContent;
    }

    String NotificationContent;
}
