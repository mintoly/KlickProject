package com.example.KlickApp.ui.resultpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;
import com.example.KlickApp.MainActivity;

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
    private FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        binding = ResultPageBinding.inflate(getLayoutInflater());

        intent = getIntent();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( ResultActivity.this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 0 );
        } else {
            //TODO: location from mainactivity
            Location location = MainActivity.gpsTracker.getLocation();
            /*Location location = new Location("current");
            location.setLatitude(37.330689);
            location.setLongitude(126.5930664); // For emulator */
          // For Pysical device


                        Location rlocation = new Location("Target");
                        CollectionReference colRef = db.collection("store");
                        Query query;
                        if (intent.hasExtra("type")) {
                            query = colRef.whereEqualTo("type", intent.getStringExtra("type"));
                        } else {
                            // TODO: name searching algorithm
                            query = colRef.whereEqualTo("name", intent.getStringExtra("name"));
                        }
                        // get database
                        query.get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {


                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, "Document is" + document.getData());
                                            ResultItem r = new ResultItem(document.getString("name"), document.getString("type"));
                                            rlocation.setLatitude(document.getDouble("x"));
                                            rlocation.setLongitude(document.getDouble("y"));
                                            if (location != null) {
                                                r.setDistance((double) location.distanceTo(rlocation));
                                            } else {
                                                Log.d(TAG, "로케이션이 null");
                                            }
                                            r.setId(document.getId());
                                            StorageReference gsReference = storage.getReferenceFromUrl(document.getString("image_link"));
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
                if (o1.getDistance() < o2.getDistance()) {
                    return -1;
                } else if (o1.getDistance() > o2.getDistance()) {
                    return 1;
                } else {
                    return 0;
                }
            };
            binding.textSortingStandard.setText("name"); // TODO: 이름 안뜨는거 고치기

            Collections.sort(resultItemArrayList, nameComparator);
            //adaptor.addItem("Test", "Testcontext");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                Collections.sort(resultItemArrayList, distanceComparator);
                binding.textSortingStandard.setText("distance");
                break;
            case R.id.item_name_result_menu:
                Collections.sort(resultItemArrayList, nameComparator);
                binding.textSortingStandard.setText("name");

                break;
        }


        return super.onContextItemSelected(item);
    }
}
