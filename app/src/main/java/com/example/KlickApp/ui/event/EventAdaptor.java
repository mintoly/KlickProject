package com.example.KlickApp.ui.event;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.KlickApp.R;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.ViewHolder> {
    private ArrayList<StorageReference> bannerUrlList = new ArrayList<>();

    public EventAdaptor(ArrayList<StorageReference> b) {bannerUrlList = b;}

    public void setBannerUrlList(ArrayList<StorageReference> bannerUrlList) {
        this.bannerUrlList = bannerUrlList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        EventAdaptor.ViewHolder viewHolder = new EventAdaptor.ViewHolder(inflater.inflate(R.layout.event_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        bannerUrlList.get(position).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Image link is : " + task.getResult().toString());
                        Glide.with(holder.itemView.getContext())
                                .load(task.getResult())

                                .fitCenter()
                                .into(holder.banner);
                    } else {
                        Log.d(TAG, "Image download error : "+task.getException().toString());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return bannerUrlList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView banner;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.image_event);
        }
    }

    public void add(StorageReference ref) {
        bannerUrlList.add(ref);
        Log.d(TAG, "adapter inserted : " + ref.toString());
        this.notifyItemInserted(bannerUrlList.size());
        this.notifyDataSetChanged();
    }
}
