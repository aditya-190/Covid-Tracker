package com.aditya.covid19;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class splashScreen extends AppCompatActivity {
    public static String currentLocation;

    // Intro Screen of the App that will be displayed.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        checkConnection();
        getLocation();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(splashScreen.this, MainActivity.class));
            finish();
        }, 200);
    }

    public void checkConnection() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_internet_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView checkInternet = dialog.findViewById(R.id.check_internet);
        TextView goToSettings = dialog.findViewById(R.id.goto_settings);

        goToSettings.setOnClickListener(v -> {
            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
        });

        checkInternet.setOnClickListener(v -> {
            if (isOnline())
                dialog.dismiss();
        });

        if (isOnline())
            dialog.dismiss();
        else
            dialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    public void getLocation() {
        Dialog dialog = new Dialog(splashScreen.this);
        dialog.setContentView(R.layout.enable_location);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView checkLocation = dialog.findViewById(R.id.check_location);
        TextView goToLocationSettings = dialog.findViewById(R.id.goto_location);

        goToLocationSettings.setOnClickListener(v -> {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        });

        checkLocation.setOnClickListener(v -> {
            if (isLocationEnabled())
                dialog.dismiss();
        });

        if (isLocationEnabled())
            dialog.dismiss();
        else
            dialog.show();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(splashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(splashScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }
                });
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(splashScreen.this, Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    splashScreen.currentLocation = addressList.get(0).getAdminArea();
                } catch (IOException e) {
                    Log.d("IOException", "Error = " + e);
                }
            } else {
                splashScreen.currentLocation = "None";
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) splashScreen.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        return gps_enabled && network_enabled;
    }
}