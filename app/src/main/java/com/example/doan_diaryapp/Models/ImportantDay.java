package com.example.doan_diaryapp.Models;

public class ImportantDay {
    int Id;
    int EntryId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public ImportantDay(int id, int entryId) {
        Id = id;
        EntryId = entryId;
    }
}
