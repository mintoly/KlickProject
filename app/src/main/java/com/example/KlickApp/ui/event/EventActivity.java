package com.example.KlickApp.ui.event;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.EventPageBinding;
import com.example.KlickApp.ui.home.event.BannerAdaptor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EventActivity extends AppCompatActivity {

    EventPageBinding binding;
    FirebaseFirestore db;
    FirebaseStorage storage;
    EventAdaptor adaptor;
    ArrayList<StorageReference> bannerUrlList;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);
        binding = EventPageBinding.inflate(getLayoutInflater());

        bannerUrlList = new ArrayList<>();
        adaptor = new EventAdaptor(bannerUrlList);
        binding.viewEventPage.setAdapter(adaptor);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        db.collection("event")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "event collection accessed!");
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Log.d(TAG, snapshot.getData().toString());
                            Log.d(TAG, "The gs Link is : " + snapshot.getData().get("banner").toString());
                            StorageReference ref = storage.getReferenceFromUrl(snapshot.getData().get("banner").toString());
                            adaptor.add(ref);

                        }
                        Log.d(TAG, bannerUrlList.toString());
                    } else {
                        Log.d(TAG, "event collection error!" + task.getException().toString());
                    }
                });

        Log.d(TAG, "bannerUri? : " + bannerUrlList.toString());

        binding.viewEventPage.setLayoutManager(new LinearLayoutManager(this));
    }



}
