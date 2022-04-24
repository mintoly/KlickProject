package com.example.KlickApp.ui.event;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                            bannerUrlList.add(ref);
                            adaptor.notifyDataSetChanged();
                        }
                        Log.d(TAG, bannerUrlList.toString());
                    } else {
                        Log.d(TAG, "event collection error!" + task.getException().toString());
                    }
                });

        adaptor = new EventAdaptor(bannerUrlList);
        Log.d(TAG, "bannerUri? : " + bannerUrlList.toString());

        RecyclerView eventView = findViewById(R.id.view_event_page);
        eventView.setAdapter(adaptor);
        eventView.setLayoutManager(new LinearLayoutManager(this));
    }



}
