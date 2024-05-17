package com.example.doan_diaryapp.Models;

public class Custom {
    public int getByear() {
        return Byear;
    }

    public void setByear(int byear) {
        Byear = byear;
    }

    public int getBmonth() {
        return Bmonth;
    }

    public void setBmonth(int bmonth) {
        Bmonth = bmonth;
    }

    public int getAyear() {
        return Ayear;
    }

    public void setAyear(int ayear) {
        Ayear = ayear;
    }

    public int getAmonth() {
        return Amonth;
    }

    public void setAmonth(int amonth) {
        Amonth = amonth;
    }

    public int getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(int statisticType) {
        this.statisticType = statisticType;
    }

    public String getEmotionType() {
        return emotionType;
    }

    public void setEmotionType(String emotionType) {
        this.emotionType = emotionType;
    }

    public Custom(int byear, int bmonth, int ayear, int amonth, int statisticType, String emotionType) {
        Byear = byear;
        Bmonth = bmonth;
        Ayear = ayear;
        Amonth = amonth;
        this.statisticType = statisticType;
        this.emotionType = emotionType;
    }

    private int Byear;
    private int Bmonth;
    private int Ayear;
    private int Amonth;
    private int statisticType;

    private String emotionType;
}
