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

    public Emotion(String icon, String descEn, String descVi) {
        Icon = icon;
        DescEn = descEn;
        DescVi = descVi;
    }

    int Id;
    String Icon;

    String DescEn;

    public String getDescEn() {
        return DescEn;
    }

    public void setDescEn(String descEn) {
        DescEn = descEn;
    }

    public String getDescVi() {
        return DescVi;
    }

    public void setDescVi(String descVi) {
        DescVi = descVi;
    }

    String DescVi;
}
