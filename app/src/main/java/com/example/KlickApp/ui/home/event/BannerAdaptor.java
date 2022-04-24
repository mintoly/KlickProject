package com.example.KlickApp.ui.home.event;

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
import com.example.KlickApp.databinding.EventBannerItemBinding;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BannerAdaptor extends RecyclerView.Adapter<BannerAdaptor.ViewHolder> {
    private ArrayList<StorageReference> bannerUrlList = new ArrayList<>();

    public BannerAdaptor(ArrayList<StorageReference> b) {bannerUrlList = b;}

    public void setBannerUrlList(ArrayList<StorageReference> bannerUrlList) {
        this.bannerUrlList = bannerUrlList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        BannerAdaptor.ViewHolder viewHolder = new BannerAdaptor.ViewHolder(inflater.inflate(R.layout.event_banner_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bindBanner(bannerUrlList.get(position));
    }

    @Override
    public int getItemCount() {
        return bannerUrlList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        EventBannerItemBinding bannerItemBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bannerItemBinding = EventBannerItemBinding.bind(itemView);
        }

        void bindBanner(StorageReference ref) {
            ref.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Image link is : " + task.getResult().toString());
                        Glide.with(itemView.getContext())
                                .load(task.getResult())

                                .fitCenter()
                                .into(bannerItemBinding.imageEventBanner);
                    } else {
                        Log.d(TAG, "Image download error : "+task.getException().toString());
                    }
                });
        }
    }

    public void add(StorageReference ref) {
        bannerUrlList.add(ref);
        Log.d(TAG, "adapter inserted : " + ref.toString());
        this.notifyItemInserted(bannerUrlList.size());
        this.notifyDataSetChanged();
    }
}
