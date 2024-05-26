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

    public Weather(String icon, String desc, String descVi, int isActive) {
        Icon = icon;
        Desc = desc;
        DescVi = descVi;
        IsActive = isActive;
    }

    String Desc;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int IsActive;
    public String getDescVi() {
        return DescVi;
    }

    public void setDescVi(String descVi) {
        DescVi = descVi;
    }

    String DescVi;
    int Id;
    String Icon;
}
