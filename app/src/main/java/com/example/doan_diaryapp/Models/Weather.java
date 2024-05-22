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

    public Weather(String icon, String descEn, String descVi, int isActive) {
        Icon = icon;
        DescEn = descEn;
        DescVi = descVi;
        IsActive = isActive;
    }

    String DescEn;

    public String getDescEn() {
        return DescEn;
    }

    public void setDescEn(String descEn) {
        DescEn = descEn;
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
