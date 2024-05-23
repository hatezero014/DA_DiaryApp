package com.example.doan_diaryapp.Models;

public class SelectedItem {
    private int id;
    private String text;

    public SelectedItem() {
    }

    public SelectedItem(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
