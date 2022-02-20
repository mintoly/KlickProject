package com.example.KlickApp.ui.resultpage.GpsTracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.ContentValues.TAG;

public class GpsTracker extends Service implements LocationListener {


    private LocationManager lm;
    private final Context mContext;
    private Location location;
    private double latitude;
    private double longitude;
    public static final int MIN_DISTANCE_CHECKING = 10;
    public static final int MIN_TIME_CHECKING = 60000;

    public GpsTracker(Context c) {
        mContext = c;
        setLocation();
    }


    private Location setLocation() throws NullPointerException {
        try {
            lm = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // Case 1
            /*
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled || isNetworkEnabled) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkEnabled) {
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_CHECKING, MIN_DISTANCE_CHECKING, this);
                        if (lm != null) {
                            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            Log.d(TAG, "Location update complete! by network!");
                        }
                    } else if (isGPSEnabled) {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHECKING, MIN_DISTANCE_CHECKING, this);
                        if (lm != null) {
                            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d(TAG, "Location update complete! by GPS!");
                        }
                    } else {
                        Log.d(TAG, "There are missing permission!");
                    }
                }
            }*/

            // Case 2
            /*lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = lm.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = lm.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            location = bestLocation;*/

            //Case 3

            LocationRequest lr = LocationRequest.create();
            lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            lr.setInterval(10 * 1000);
            LocationCallback lc = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult == null) return;
                    Location bestLocation = null;

                    for (Location l : locationResult.getLocations()) {
                        if (l != null) {
                            if (bestLocation == null || bestLocation.getAccuracy() < l.getAccuracy()) {
                                bestLocation = l;
                            }
                        }
                    }
                    location = bestLocation;
                }
            };
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            fusedLocationProviderClient.requestLocationUpdates(lr, lc, Looper.getMainLooper());

            //TODO: location returns null, please fix it

            //Case 4
            /*FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location1 -> {
                        if (location1 != null) {
                            location = location1;
                        } else {
                            throw new NullPointerException();
                        }
                    });*/
        } catch (Exception e) {
            Log.d(TAG, ""+e.toString());
        }
        return location;
    }

    private boolean isPermissionAvailable() {

        return false;
    }
    private void getPermission() {

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
            setLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public double getLatitude(){
        return location.getLatitude();
    }
    public double getLongitude() {
        return location.getLongitude();
    }
    public Location getLocation() {
        return location;
    }
    public void removeUpdate() {
        lm.removeUpdates(this);
    }
}
