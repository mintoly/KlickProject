package com.example.KlickApp.ui.review.write;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.KlickApp.databinding.ReviewWritePageBinding;
import com.example.KlickApp.ui.review.ReviewItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static android.content.ContentValues.TAG;


public class ReviewWriteActivity extends AppCompatActivity {

    ReviewWritePageBinding binding;
    private String storeId;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReviewWritePageBinding.inflate(getLayoutInflater());

        storeId = getIntent().getStringExtra("store_id");

        binding.btnReviewWriteConfirm.setOnClickListener(v -> {
            ReviewItem reviewItem = new ReviewItem();
            reviewItem.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            reviewItem.setRating(binding.ratingReviewWrite.getRating());
            reviewItem.setContent(binding.editReviewWrite.getText().toString());
            reviewItem.setCosts(Long.parseLong(binding.editReviewWriteCost.getText().toString()));
            reviewItem.setDate(new Date());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("review")
                    .add(reviewItem.getData())
                    .addOnSuccessListener(docRef -> {
                        db.collection("store")
                                .document(storeId)
                                .update("review", FieldValue.arrayUnion(docRef.getId()))
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Review confirmed!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Log.d(TAG, "reviewwrite store adding error : " + task.getException());
                                    }
                                });
                    });
        });
        setContentView(binding.getRoot());
    }
}
