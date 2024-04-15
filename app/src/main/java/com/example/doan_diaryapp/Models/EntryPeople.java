package com.example.doan_diaryapp.Models;

public class EntryPeople {
    int PeopleId;
    int EntryId;

    public int getPeopleId() {
        return PeopleId;
    }

    public void setPeopleId(int peopleId) {
        PeopleId = peopleId;
    }

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public EntryPeople(int peopleId, int entryId) {
        PeopleId = peopleId;
        EntryId = entryId;
    }
}
