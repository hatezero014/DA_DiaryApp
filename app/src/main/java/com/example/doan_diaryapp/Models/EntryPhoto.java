package com.example.doan_diaryapp.Models;

public class EntryPhoto {
    int EntryId;
    byte[] Photo;

    public EntryPhoto() {
    }

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }

    public EntryPhoto(int entryId, byte[] photo) {
        EntryId = entryId;
        Photo = photo;
    }
}
