package com.example.KlickApp.ui.resultpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.KlickApp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ResultAdaptor extends RecyclerView.Adapter<ResultAdaptor.ViewHolder> {

    //private ArrayList<ResultItem> ResultItemList = new ArrayList<ResultItem>();
    private ArrayList<ResultItem> showItemList = new ArrayList<>();
    private Context context;

    ResultAdaptor(ArrayList<ResultItem> r) {
        showItemList = r;
    }

    @NonNull
    @NotNull
    @Override
    public ResultAdaptor.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.result_item, parent, false);
        ResultAdaptor.ViewHolder viewHolder = new ResultAdaptor.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ResultItem item = showItemList.get(position);

        holder.content.setText(item.getContent());
        holder.icon.setImageDrawable(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.distance.setText(item.getDistance());
    }


    @Override
    public int getItemCount() {
        return showItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView title;
        TextView content;
        TextView distance;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.img_item);
            title = itemView.findViewById(R.id.text_item_title);
            content = itemView.findViewById(R.id.text_item_content);
            distance = itemView.findViewById(R.id.text_item_distance);
        }
    }

    //Below: For test
    public void addItem(String t, String c) {
        ResultItem r = new ResultItem(t,c);
        showItemList.add(r);
    }






}
