package com.example.doan_diaryapp.Models;

public class Activity {
    int Id;
    String Icon;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Activity() {
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public Activity(String icon) {
        Icon = icon;
    }
}
