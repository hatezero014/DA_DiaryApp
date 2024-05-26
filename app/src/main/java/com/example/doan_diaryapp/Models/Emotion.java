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

    public Emotion(String icon, String desc, String descVi, int isActive) {
        Icon = icon;
        Desc = desc;
        DescVi = descVi;
        IsActive = isActive;
    }


    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int IsActive;

    int Id;
    String Icon;

    String Desc;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getDescVi() {
        return DescVi;
    }

    public void setDescVi(String descVi) {
        DescVi = descVi;
    }

    String DescVi;
}
