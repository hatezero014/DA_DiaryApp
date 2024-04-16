package com.example.doan_diaryapp.Models;

public class Language {
    int Id;
    String Code;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public Language() {
    }

    public Language(String name, String code, int isActive) {
        Code = code;
        Name = name;
        IsActive = isActive;
    }

    String Name;
    int IsActive;
}
