package com.example.KlickApp.ui.resultpage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.ResultPageBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Comparator;

import static android.content.ContentValues.TAG;

public class ResultActivity extends AppCompatActivity {

    private ResultAdaptor adaptor;
    private Intent intent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<ResultItem> resultItemArrayList = new ArrayList<ResultItem>();
    private RecyclerView resultView;
    private ResultPageBinding binding;
    private Comparator<ResultItem> nameComparator;
    private Comparator<ResultItem> distanceComparator;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        binding = ResultPageBinding.inflate(getLayoutInflater());


    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        CollectionReference colRef = db.collection("seller");
        Query query;
        if (intent.hasExtra("type")) {
            query = colRef.whereEqualTo("type", intent.getStringExtra("type"));
        } else {
            // TODO: name searching algorithm
            query = colRef.whereEqualTo("name", intent.getStringExtra("name"));
        }
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "Document is" + document.getData());
                            ResultItem r = new ResultItem(document.getString("name"), document.getString("type"));

                            StorageReference gsReference = storage.getReference().child(document.getString("image_link"));
                            if (gsReference == null) {
                                Log.d(TAG, "There is no image");
                            }
                                gsReference.getBytes(1024 * 1024)
                                        .addOnSuccessListener(bytes -> {

                                            BitmapDrawable image;
                                            image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                            Log.d(TAG, "image is??" + bytes.length + image.getIntrinsicHeight() + image.getIntrinsicWidth());

                                            r.setIcon(image);

                                            resultItemArrayList.add(r);
                                            Log.d(TAG, "ITEM is?" + r.getTitle() + " " + r.getContent() + " " + resultItemArrayList.size());
                                            adaptor.notifyDataSetChanged();

                                        }).addOnFailureListener(exc -> {
                                    Log.d(TAG, "Image error?" + exc.getMessage());
                                });
                            }
                        } else {
                            Log.d(TAG, "document Error!", task.getException());
                        }
                    });
            Log.d(TAG, "ITEM is???" + resultItemArrayList.size());

        
        adaptor = new ResultAdaptor(resultItemArrayList);

        resultView = findViewById(R.id.view_result);

        resultView.setAdapter(adaptor);
        resultView.setLayoutManager(new LinearLayoutManager(this));

        registerForContextMenu(binding.textSortingStandard);

        nameComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        distanceComparator = (o1, o2) -> {
            if (Double.parseDouble(o1.getDistance()) < Double.parseDouble(o2.getDistance())) {
                return - 1;
            } else if (Double.parseDouble(o1.getDistance()) > Double.parseDouble(o2.getDistance())) {
                return 1;
            } else {
                return 0;
            }
        };
        //adaptor.addItem("Test", "Testcontext");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_page_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item_distance_result_menu:

                break;
            case R.id.item_name_result_menu:

                break;
        }


        return super.onContextItemSelected(item);
    }
}
