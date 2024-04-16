package com.example.doan_diaryapp.Models;

public class ImportantDay {
    int Id;
    String Date;

    public ImportantDay() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ImportantDay(String date) {
        Date = date;
    }
}
