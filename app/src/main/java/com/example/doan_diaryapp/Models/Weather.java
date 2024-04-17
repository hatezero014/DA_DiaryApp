package com.example.doan_diaryapp.Models;

public class Weather {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public Weather() {
    }

    public Weather(String icon) {
        Icon = icon;
    }

    int Id;
    String Icon;
}
