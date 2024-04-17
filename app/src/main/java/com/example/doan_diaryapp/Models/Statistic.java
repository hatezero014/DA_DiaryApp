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

    public Statistic(int year, int month, int statisticType) {
        this.year = year;
        this.month = month;
        this.statisticType = statisticType;
    }

    public Statistic(int year, int statisticType) {
        this.year = year;
        this.month = 0;
        this.statisticType = statisticType;
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
