package com.example.doan_diaryapp.Models;

public class Entry {
    int Id;
    String Note;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    String Title;

    public Entry() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getOverallScore() {
        return OverallScore;
    }

    public void setOverallScore(int overallScore) {
        OverallScore = overallScore;
    }

    public Entry(String title, String note, String date, int overallScore) {
        Title = title;
        Note = note;
        Date = date;
        OverallScore = overallScore;
    }

    public Entry(int id, String note, String date,String title) {
        Id = id;
        Note = note;
        Date = date;
        Title =title;
    }

    public Entry(int overallScore, String date){
        OverallScore = overallScore;
        Date = date;
    }

    String Date;
    int OverallScore;
}
