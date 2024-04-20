package com.example.doan_diaryapp.Models;

public class Statistic {
    private int year;
    private int month;

    public int getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(int statisticType) {
        this.statisticType = statisticType;
    }

    private int statisticType;

    private String emotionType;

    public String getEmotionType() {
        return emotionType;
    }

    public void setEmotionType(String emotionType) {
        this.emotionType = emotionType;
    }

    public Statistic(int year, int month, int statisticType, String emotionType) {
        this.year = year;
        this.month = month;
        this.statisticType = statisticType;
        this.emotionType = emotionType;
    }

    public Statistic(int year, int statisticType, String emotionType) {
        this.year = year;
        this.month = 0;
        this.statisticType = statisticType;
        this.emotionType =emotionType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
