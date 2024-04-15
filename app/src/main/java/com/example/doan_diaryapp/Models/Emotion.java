package com.example.doan_diaryapp.Models;

public class Emotion {
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Emotion(int id, String icon, String description) {
        Id = id;
        Icon = icon;
        Description = description;
    }

    int Id;
    String Icon;
    String Description;
}
