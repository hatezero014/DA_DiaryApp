package com.example.doan_diaryapp.Models;

public class EntryEmotion {
    int EntryId;
    int EmotionId;

    public EntryEmotion() {
    }

    public EntryEmotion(int entryId, int emotionId) {
        EntryId = entryId;
        EmotionId = emotionId;
    }

    public EntryEmotion(int emotionId) {
        EmotionId = emotionId;
    }

    public int getEntryId() {
        return EntryId;
    }

    public void setEntryId(int entryId) {
        EntryId = entryId;
    }

    public int getEmotionId() {
        return EmotionId;
    }

    public void setEmotionId(int emotionId) {
        EmotionId = emotionId;
    }
}
