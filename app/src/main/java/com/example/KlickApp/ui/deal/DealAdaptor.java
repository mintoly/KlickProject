package com.example.KlickApp.ui.deal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.KlickApp.R;
import com.example.KlickApp.ui.home.event.BannerAdaptor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DealAdaptor extends RecyclerView.Adapter<DealAdaptor.ViewHolder>{

    private ArrayList<DealItem> dealItemList;

    public DealAdaptor(ArrayList<DealItem> r) {dealItemList = r;}

    public void setDealItemList(ArrayList<DealItem> dealItemList) {
        this.dealItemList = dealItemList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DealAdaptor.ViewHolder viewHolder = new DealAdaptor.ViewHolder(inflater.inflate(R.layout.todays_deal_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.title.setText(dealItemList.get(position).getTitle());
        holder.discount.setText(dealItemList.get(position).getDiscount().toString());
        holder.distance.setText(dealItemList.get(position).getDistance().toString());

        //TODO: DATE exchange
        //holder.dealEnd.setText(dealItemList.get(position).getDealEnd());
        dealItemList.get(position).getIcon().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    Glide.with(holder.itemView.getContext())
                            .load(task.getResult())
                            .into(holder.icon);

                });

        holder.dealEnd.setText(dealItemList.get(position).getDealEnd().toString());



    }

    @Override
    public int getItemCount() {
        return dealItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView discount;
        TextView dealEnd;
        TextView distance;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.img_deal_store);
            title = itemView.findViewById(R.id.text_deal_store);
            discount = itemView.findViewById(R.id.text_deal_discount);
            dealEnd = itemView.findViewById(R.id.text_deal_date);
            distance = itemView.findViewById(R.id.text_deal_distance);
        }
    }
}
