package com.example.doan_diaryapp.Models;

public class Setting {
    int RemindStatus;
    String Lock;

    public int getRemindStatus() {
        return RemindStatus;
    }

    public void setRemindStatus(int remindStatus) {
        RemindStatus = remindStatus;
    }

    public String getLock() {
        return Lock;
    }

    public void setLock(String lock) {
        Lock = lock;
    }

    public Setting(int remindStatus, String lock) {
        RemindStatus = remindStatus;
        Lock = lock;
    }
}
