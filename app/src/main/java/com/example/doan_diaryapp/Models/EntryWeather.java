package com.example.doan_diaryapp.Models;

public class EntryWeather {
    int EntryId;
    int WeatherId;

    public EntryWeather() {
    }

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public int getWeatherId() {
        return WeatherId;
    }

    public void setWeatherId(int weatherId) {
        WeatherId = weatherId;
    }

    public EntryWeather(int entryId, int weatherId) {
        EntryId = entryId;
        WeatherId = weatherId;
    }
}
