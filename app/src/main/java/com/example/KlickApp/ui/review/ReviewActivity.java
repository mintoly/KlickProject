package com.example.KlickApp.ui.review;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.KlickApp.databinding.ReviewPageBinding;
import com.example.KlickApp.ui.review.write.ReviewWriteActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ReviewActivity extends AppCompatActivity {

    private Intent intent;
    private ReviewPageBinding binding;
    private String storeId;
    private FirebaseFirestore db;
    private ArrayList<ReviewItem> reviewList = new ArrayList<>();
    public ReviewAdaptor adaptor;
    private ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReviewPageBinding.inflate(getLayoutInflater());

        intent = getIntent();
        storeId = intent.getStringExtra("store_id");

        db = FirebaseFirestore.getInstance();

        db.collection("store")
                .document(storeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> reviewIds = (ArrayList<String>)(task.getResult().get("review"));



                        Collections.reverse(reviewIds);
                        for (String reviewId : reviewIds) {
                            db.collection("review")
                                    .document(reviewId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            ReviewItem item = new ReviewItem(task1.getResult().getData());
                                            FirebaseStorage storage = FirebaseStorage.getInstance();

                                            db.collection("user")
                                                    .document(item.getUserId())
                                                    .get()
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            item.setUserName(task2.getResult().get("name").toString());
                                                            if (task1.getResult().get("thumb") != null) {
                                                                StorageReference gsReference = storage.getReferenceFromUrl(task1.getResult().get("thumb").toString());

                                                                if (gsReference == null) {
                                                                    Log.d(TAG, "Thumb getting error : not image");
                                                                } else {
                                                                    gsReference.getBytes(1024 * 1024)
                                                                            .addOnSuccessListener(bytes -> {
                                                                                BitmapDrawable image;
                                                                                image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                                                                Log.d(TAG, "review - thumb - image is??" + bytes.length + image.getIntrinsicHeight() + image.getIntrinsicWidth());

                                                                                item.setIcon(image);
                                                                                reviewList.add(item);
                                                                                adaptor.notifyDataSetChanged();
                                                                            });
                                                                }
                                                            } else {
                                                                reviewList.add(item);
                                                                adaptor.notifyDataSetChanged();
                                                            }
                                                        } else {
                                                            Log.d(TAG, "review - user get error : " + task2.getException().toString());
                                                        }

                                                    });
                                        } else {
                                            Log.d(TAG, "review get error! : " + task1.getException().toString());
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "store get error! : " + task.getException().toString());
                    }
                });
        adaptor = new ReviewAdaptor(reviewList);
        binding.viewReview.setAdapter(adaptor);
        binding.viewReview.setLayoutManager(new LinearLayoutManager(this));

        //TODO: review write 완료
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            finish();
                        }
                    }
                }
        );


        binding.btnReviewWrite.setOnClickListener(v -> {
            Intent intent1 = new Intent(ReviewActivity.this, ReviewWriteActivity.class);
            intent1.putExtra("store_id", storeId);
            resultLauncher.launch(intent1);
        });
        setContentView(binding.getRoot());
    }
}
