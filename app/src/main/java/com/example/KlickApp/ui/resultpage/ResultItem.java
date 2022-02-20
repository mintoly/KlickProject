package com.example.KlickApp.ui.resultpage;

import android.graphics.drawable.Drawable;

public class ResultItem {
    private Drawable icon;
    private String title;
    private String content;
    private String id;
    private Double distance = 0.0;

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

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getDistance() {
        return distance;
    }

    public String getId() {return id;};
}
