package com.example.doan_diaryapp.Models;

public class Partner {
    int Id;
    String Icon;

    public Partner() {
    }

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

    public Partner(String icon) {
        Icon = icon;
    }
}
