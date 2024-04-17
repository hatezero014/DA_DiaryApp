package com.example.doan_diaryapp.Models;

public class EntryPhoto {
    int EntryId;
    String Photo;

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public EntryPhoto() {
    }

    public EntryPhoto(int entryId, String photo) {
        EntryId = entryId;
        Photo = photo;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
