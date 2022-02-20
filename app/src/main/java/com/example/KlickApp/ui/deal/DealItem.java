package com.example.KlickApp.ui.deal;

import android.graphics.drawable.Drawable;

import com.google.firebase.Timestamp;
import com.google.firebase.storage.StorageReference;


public class DealItem {

    StorageReference icon;
    String title;
    Double discount;
    Timestamp dealEnd;
    Double distance;

    public DealItem(StorageReference icon, String title, Double discount, Timestamp dealEnd, Double distance) {
        this.icon = icon;
        this.title = title;
        this.discount = discount;
        this.dealEnd = dealEnd;
        this.distance = distance;
    }

    public StorageReference getIcon() {
        return icon;
    }

    public void setIcon(StorageReference icon) {
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Timestamp getDealEnd() {
        return dealEnd;
    }

    public void setDealEnd(Timestamp deal_end) {
        this.dealEnd = deal_end;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
