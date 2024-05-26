package com.example.doan_diaryapp.Models;

public class Activity {
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Activity() {
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int IsActive;

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public Activity(String icon, String desc, String descVi, int isActive) {
        Icon = icon;
        Desc = desc;
        DescVi = descVi;
        IsActive = isActive;
    }
}
