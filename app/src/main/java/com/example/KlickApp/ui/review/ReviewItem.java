package com.example.KlickApp.ui.review;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.ContentValues.TAG;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewItem {
    private Drawable icon;
    private String userId;
    private String userName;
    private Date date;
    private double rating;
    private long cost;
    private String content;

    public ReviewItem() {
    }

    public ReviewItem(Map<String, Object> doc) {
        setContent(doc.get("contents").toString());
        setCosts((long)doc.get("costs"));
        setDate(((Timestamp)doc.get("date")).toDate());
        setUserId(doc.get("email").toString());
        setRating(Double.parseDouble(String.valueOf(doc.get("rating"))));



    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public long getCosts() {
        return cost;
    }

    public void setCosts(long costs) {
        this.cost = costs;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public double getRating() {
        return rating;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("contents", getContent());
        map.put("costs", getCosts());
        map.put("email", getUserId());
        map.put("rating", getRating());
        map.put("date", new Timestamp(getDate()));
        return map;
    }
}
