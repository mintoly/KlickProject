package com.example.KlickApp.ui.searchpage;

import android.graphics.drawable.Drawable;

public class SearchItem {
    private Drawable icon;
    private String title;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getContent() {
        return content;
    }
}
