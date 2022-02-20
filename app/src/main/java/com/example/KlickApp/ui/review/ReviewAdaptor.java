package com.example.KlickApp.ui.review;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.ReviewItemBinding;
import com.example.KlickApp.ui.resultpage.ResultAdaptor;
import com.example.KlickApp.ui.resultpage.ResultItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ViewHolder> {

    private ArrayList<ReviewItem> showItemList;
    private FirebaseFirestore db;

    public ReviewAdaptor(ArrayList<ReviewItem> i) {showItemList = i;}
    @NonNull
    @NotNull
    @Override

    public ReviewAdaptor.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.review_item, parent, false);
        ReviewAdaptor.ViewHolder viewHolder = new ReviewAdaptor.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ReviewItem item = showItemList.get(position);



        holder.content.setText(item.getContent());
        holder.icon.setImageDrawable(item.getIcon());
        holder.name.setText(item.getUserName());
        holder.rating.setText(Double.toString(item.getRating()) + " / 5.0");
        holder.cost.setText(Long.toString(item.getCosts()));
        holder.content.setText(item.getContent());

        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("email", item.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            holder.level.setText(doc.getLong("level").toString());
                        }
                    } else {
                        Log.d(TAG, "user level get error: " + task.getException().toString());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return showItemList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        TextView level;
        TextView date;
        TextView rating;
        TextView cost;
        TextView content;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.img_review);
            name = itemView.findViewById(R.id.text_review_user_name);
            level = itemView.findViewById(R.id.text_review_user_level);
            date = itemView.findViewById(R.id.text_review_date);
            rating = itemView.findViewById(R.id.text_review_rating);
            cost = itemView.findViewById(R.id.text_review_cost);
            content = itemView.findViewById(R.id.text_review_contents);

        }
    }

}
