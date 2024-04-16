package com.example.doan_diaryapp.Models;

public class EntryPartner {
    int PartnerId;
    int EntryId;

    public EntryPartner() {
    }


    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public int getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(int partnerId) {
        PartnerId = partnerId;
    }

    public EntryPartner(int entryId, int partnerId) {
        PartnerId = partnerId;
        EntryId = entryId;
    }
}
