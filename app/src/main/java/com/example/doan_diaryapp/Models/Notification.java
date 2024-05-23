package com.example.doan_diaryapp.Models;

public class Notification {
    int Id;
    String Time;
    String Day;
    int Content;

    String Sub;

    public String getSub() {
        return Sub;
    }

    public void setSub(String sub) {
        Sub = sub;
    }

    public Notification() {
    }

    public Notification(String time, String day, int content, String sub) {
        Time = time;
        Day = day;
        Content = content;
        Sub = sub;
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

    public int getContent() {
        return Content;
    }

    public void setContent(int content) {
        Content = content;
    }
}
