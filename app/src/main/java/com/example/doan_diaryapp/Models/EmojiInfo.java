package com.example.doan_diaryapp.Models;

public class EmojiInfo {
    private String icon;
    private String iconCount;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconCount() {
        return iconCount;
    }

    public void setIconCount(String iconCount) {
        this.iconCount = iconCount;
    }

    public EmojiInfo(String icon, String iconCount) {
        this.icon = icon;
        this.iconCount = iconCount;
    }
}
