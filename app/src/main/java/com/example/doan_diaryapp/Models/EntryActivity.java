package com.example.doan_diaryapp.Models;

public class EntryActivity {
    int EntryId;
    int ActivityId;

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public int getActivityId() {
        return ActivityId;
    }

    public void setActivityId(int activityId) {
        ActivityId = activityId;
    }

    public EntryActivity(int entryId, int activityId) {
        EntryId = entryId;
        ActivityId = activityId;
    }
}
