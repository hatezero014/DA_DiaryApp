package com.example.doan_diaryapp.Models;

public class Entry {
    int Id;
    String Note;

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

    public String getWakeUp() {
        return WakeUp;
    }

    public void setWakeUp(String wakeUp) {
        WakeUp = wakeUp;
    }

    public String getSleep() {
        return Sleep;
    }

    public void setSleep(String sleep) {
        Sleep = sleep;
    }

    public Entry(String note, String date, int overallScore, String wakeUp, String sleep) {
        Note = note;
        Date = date;
        OverallScore = overallScore;
        WakeUp = wakeUp;
        Sleep = sleep;
    }

    public Entry(int id, String note, String date) {
        Id = id;
        Note = note;
        Date = date;
    }

    public Entry(int overallScore, String date){
        OverallScore = overallScore;
        Date = date;
    }

    String Date;
    int OverallScore;
    String WakeUp;
    String Sleep;
}
