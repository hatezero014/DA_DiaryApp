package com.example.doan_diaryapp.Models;

public class Emotion {
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Emotion() {
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public Emotion(String icon) {
        Icon = icon;
    }

    int Id;
    String Icon;
}
