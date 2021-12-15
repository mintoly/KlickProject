package com.example.KlickApp.ui.resultpage;

import android.graphics.drawable.Drawable;

public class ResultItem {
    private Drawable icon;
    private String title;
    private String content;
    private String distance;

    ResultItem(Drawable d, String t, String c) {
        this.setContent(c);
        this.setIcon(d);
        this.setTitle(t);
    }
    ResultItem(String t, String c) {
        this.setTitle(t);
        this.setContent(c);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getDistance() {
        return distance;
    }
}
