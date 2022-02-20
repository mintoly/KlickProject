package com.example.KlickApp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.KlickApp.ui.event.EventActivity;
import com.example.KlickApp.ui.resultpage.GpsTracker.GpsTracker;
import com.example.KlickApp.ui.resultpage.ResultActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.KlickApp.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.KlickApp.ui.userpage.UserFragment.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public static GpsTracker gpsTracker;
    private ActivityMainBinding binding;
    private String[] REQUIRED_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    //임시
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    class ButtonOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            //String type = "";
            switch (v.getId()) {
                case R.id.testButton:
                    try {
                        //TODO: location service - Fix it
                        /*Location l = gpsTracker.getLocation();
                        Toast.makeText(MainActivity.this, String.format("%f", l.getLatitude()), Toast.LENGTH_SHORT ).show();*/

                        //임시
                        /*
                        db.collection("seller").
                                get().
                                addOnCompleteListener(task -> {
                                    Log.d(TAG, "ㅁㄴㅇㄻㄴㄻㄴㄹㅇㄴㅁ");
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            db.collection("store")
                                                    .add(doc.getData())
                                                    .addOnSuccessListener(documentReference -> {
                                                        Log.d(TAG, "DocRef" + documentReference.getId());
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.d(TAG, "addfail" + e.toString());
                                                    });
                                        }
                                    } else {
                                        Log.d(TAG, "taskfail" + task.getException().toString());
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "getfail" + e.toString());
                                });*/
                        //

                    } catch (NullPointerException e) {
                        Toast.makeText(MainActivity.this, "Please wait until the location identify. This will take a few minuets.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
            }
            //intent.putExtra("type", type);
            //startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gpsTracker = new GpsTracker(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_event, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ButtonOnClickListner buttonOnClickListner = new ButtonOnClickListner();

        binding.testButton.setOnClickListener(buttonOnClickListner);
        binding.btnBottomEventProgress.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EventActivity.class));
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isLocationEnabled()) {
            setLocationEnabled();
        } else {
            getPermission();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    ((ImageView)findViewById(R.id.img_register)).setImageBitmap(img);


                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager lm = ((LocationManager) getSystemService(LOCATION_SERVICE));
        Log.d(TAG, String.format("%b, %b",lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER), lm.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setLocationEnabled() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        ActivityResultLauncher<Intent> settingLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (isLocationEnabled()) {
                        Log.d(TAG, "GPS 활성화 - setLocationEnabled");
                        getPermission();
                    } else {
                        Log.d(TAG, "GPS 허용 과정에 문제 - setLocationEnabled");
                    }



        });
        dialogBuilder.setMessage(R.string.alert_context_location)
                .setPositiveButton(R.string.alert_btn_location_pos, (dialog, id) -> {
                    Intent GPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    settingLauncher.launch(GPSSettingIntent);
                })
                .setNegativeButton(R.string.alert_btn_location_neg, (dialog, id) -> {

                });
        dialogBuilder.create().show();
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION[1]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION[0])) {
                    Toast.makeText(this, "위치 정보 요청을 수락해주세요", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, 0);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length == REQUIRED_PERMISSION.length) {
            boolean isit = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isit = false;
                }
            }
            if (!isit) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION[1])) {

                    Toast.makeText(MainActivity.this, "정보 제공 후 재실행 필요", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "정보 제공 허용", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}