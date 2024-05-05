package com.example.doan_diaryapp.Models;

public class Notification {
    int Id;
    String Time;
    String Day;
    String Content;

    public Notification() {
    }

    public Notification(String time, String day, String content) {
        Time = time;
        Day = day;
        Content = content;
    }

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

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
