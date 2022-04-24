package com.example.KlickApp.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.KlickApp.R;
import com.example.KlickApp.databinding.ActivityMainBinding;
import com.example.KlickApp.databinding.StorePageBinding;
import com.example.KlickApp.ui.review.ReviewActivity;
import com.example.KlickApp.ui.review.ReviewItem;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class StoreActivity extends AppCompatActivity {
    private StorePageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String storeId;
    private Map<String, Object> doc;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        storeId = intent.getStringExtra("store_id");
        binding = StorePageBinding.inflate(getLayoutInflater());
        binding.textStoreRating.setText("왜안돼???");
        db.collection("store")
                .document(storeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        doc = task.getResult().getData();
                        Log.d(TAG, doc.toString());

                        Glide.with(this)
                                .load(storage.getReferenceFromUrl(doc.get("image_link").toString()))
                                .into(binding.imageStoreThumb);
                        binding.textStoreTitle.setText(doc.get("name").toString());
                        binding.textStoreNumber.setText(doc.get("number").toString());
                        binding.textStoreRating.setText(String.format("%.1f", ((Long)doc.get("rating")).doubleValue()));
                        binding.textStoreClick.setText(doc.get("click").toString());

                        //임시
                        binding.textStoreClick.setVisibility(View.INVISIBLE);


                        //TODO: menu, review 수정사항 맞게 고치기
                        //menu context
                        List<Map<String, Object>> menu = (List<Map<String, Object>>) doc.get("menu");
                        Log.d(TAG, menu.toString());
                        for (int i = 0; i < binding.layoutStoreMenu.getChildCount(); i++) {
                            if (menu.size() <= i) {
                                binding.layoutStoreMenu.getChildAt(i).setVisibility(View.GONE);
                            } else {

                                View v = binding.layoutStoreMenu.getChildAt(i);
                                ((TextView)v.findViewById(R.id.text_menu_title)).setText((String)menu.get(i).get("name"));
                                ((TextView)v.findViewById(R.id.text_menu_costs)).setText(Long.toString((Long)menu.get(i).get("cost")) + "원");

                                Glide.with(this)
                                        .load(storage.getReferenceFromUrl(menu.get(i).get("thumb").toString()))
                                        .into((ImageView)v.findViewById(R.id.img_menu));
                            }
                        }

                        //pick context

                        Map<String, Object> kilckPick = (Map<String, Object>) doc.get("klick_pick");
                        binding.textStoreKlickPickMenu.setText(menu.get(Integer.parseInt(kilckPick.get("menu").toString())).get("name").toString());
                        binding.textStoreKlickPickContent.setText(kilckPick.get("content").toString());

                        Map<String, Object> sellerPick = (Map<String, Object>) doc.get("seller_pick");
                        binding.textStoreSellerPickMenu.setText(menu.get(Integer.parseInt(sellerPick.get("menu").toString())).get("name").toString());
                        binding.textStoreSellerPickContent.setText(sellerPick.get("content").toString());
                        //Review context
                        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //binding.viewStoreReview.addView(inflater.inflate(R.layout.review_item, null));
                        ArrayList<String> reviews = (ArrayList<String>)doc.get("review");
                        if (reviews.size() == 0) {
                            binding.layoutStoreMenu.setVisibility(View.GONE);
                        } else {
                            db.collection("review")
                                    .document(reviews.get(0))
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {


                                            Map<String, Object> reviewData = task1.getResult().getData();
                                            if (!task1.getResult().exists()) {
                                                Log.d(TAG,  "reviews : " + reviews.toString() + "reviewdata? : ");

                                            }
                                            Log.d(TAG,  "reviews : " + reviews.toString() + "reviewdata? : " + reviewData.toString());
                                            ReviewItem reviewItem = new ReviewItem(reviewData);
                                            View view = binding.layoutStoreReview;

                                            ((ImageView)view.findViewById(R.id.img_review)).setImageDrawable(reviewItem.getIcon());

                                            db.collection("user")
                                                    .document(reviewItem.getUserId())
                                                    .get()
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            ((TextView)view.findViewById(R.id.text_review_user_level)).setText(task2.getResult().get("level").toString());

                                                            ((TextView)view.findViewById(R.id.text_review_user_level)).setVisibility(View.INVISIBLE);

                                                            ((TextView)view.findViewById(R.id.text_review_user_name)).setText(task2.getResult().get("name").toString());
                                                        }
                                                    });
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(reviewItem.getDate());
                                            ((TextView)view.findViewById(R.id.text_review_date)).setText(String.format("%d.%d.%d.", c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)) );

                                            ((TextView)view.findViewById(R.id.text_review_rating)).setText(Double.toString(reviewItem.getRating()));

                                            ((TextView)view.findViewById(R.id.text_review_cost)).setText(Long.toString(reviewItem.getCosts()));
                                            ((TextView)view.findViewById(R.id.text_review_cost)).setVisibility(View.INVISIBLE);

                                            ((TextView)view.findViewById(R.id.text_review_contents)).setText(reviewItem.getContent());

                                        } else {
                                            Log.d(TAG, "review get error! : " + task1.getException());
                                        }
                                    });
                        }


                    }
                    else {
                        Log.d(TAG, task.getException().toString());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.toString());
                });
        binding.btnStoreReviewMore.setOnClickListener(v -> {
            Intent intent1 = new Intent(StoreActivity.this, ReviewActivity.class);
            intent1.putExtra("store_id", storeId );
            startActivity(intent1);
        });
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
