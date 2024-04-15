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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public  Language() {

    }

    public Language(int id, String name, String code,int isActive) {
        Id = id;
        Code = code;
        Name = name;
        this.isActive = (isActive != 0);
    }

    String Name;
    boolean isActive;
}
