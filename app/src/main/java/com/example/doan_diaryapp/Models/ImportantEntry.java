package com.example.doan_diaryapp.Models;

public class ImportantEntry {
    public ImportantEntry(int entryId) {
        EntryId = entryId;
    }

    int EntryId;

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public ImportantEntry() {
    }
}
