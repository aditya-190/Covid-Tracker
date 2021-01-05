package com.aditya.covid19.updatesSegment;

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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aditya.covid19.R;
import com.aditya.covid19.splashScreen;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class updatesFragment extends Fragment {
    private static final String JSON_URL = "https://api.covid19india.org/data.json";
    private RecyclerView recyclerView;
    private stateAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.updates_fragment, container, false);

        // Referencing
        swipeRefreshLayout = view.findViewById(R.id.updates_refresh);
        recyclerView = view.findViewById(R.id.state_recyclerView);

        // Fetch data
        fetchData();

        // Called when User Swipes down to refresh the content.
        swipeRefreshLayout.setOnRefreshListener(this::fetchData);

        return view;
    }

    private String timeFormatter(String lastUpdatedTime) throws ParseException {
        SimpleDateFormat originalTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetTimeFormat = new SimpleDateFormat("d MMM ''yy", Locale.getDefault());

        Date date = originalTimeFormat.parse(lastUpdatedTime);

        long Milliseconds = System.currentTimeMillis() - date.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(Milliseconds);

        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;

        seconds %= 3600;
        long minutes = seconds / 60;

        return "Updated " + hours + " hours, " + minutes + " minutes ago. " + targetTimeFormat.format(date);
    }

    public void checkConnection() {
        Dialog dialog = new Dialog(getActivity());
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

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        return gps_enabled && network_enabled;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    public void fetchData() {

        swipeRefreshLayout.setRefreshing(true);

        // Check Internet Connection.
        checkConnection();

        // Gets Location.
        getLocation();

        // Data is stored in this array to be used by Adapter Later on.
        ArrayList<state> allData = new ArrayList<>();

        // Volley Request for JSON API Call.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                JSON_URL, null, response -> {
            try {
                // Retrieve an JSONObject Array on StateWise Details.
                JSONArray statewise = response.getJSONArray("statewise");

                // Iterating
                for (int i = 0; i < statewise.length(); i++) {

                    // All Data for an single State.
                    JSONObject singleStateData = statewise.getJSONObject(i);

                    // Storing all required data for later use.
                    String lastUpdated = singleStateData.getString("lastupdatedtime");
                    String state = singleStateData.getString("state");
                    String active = singleStateData.getString("active");
                    String recover = singleStateData.getString("recovered");
                    String confirm = singleStateData.getString("confirmed");
                    String death = singleStateData.getString("deaths");
                    String recover_inc = singleStateData.getString("deltarecovered");
                    String death_inc = singleStateData.getString("deltadeaths");
                    String confirm_inc = singleStateData.getString("deltaconfirmed");

                    // Works when the State Name is same as we Get From our getStateLocation() function in Splash Screen.
                    if (singleStateData.getString("state").equals(splashScreen.currentLocation)) {

                        TextView myState = getActivity().findViewById(R.id.my_state);
                        TextView myStateActive = getActivity().findViewById(R.id.my_state_active);
                        TextView myStateRecover = getActivity().findViewById(R.id.my_state_recover);
                        TextView myStateConfirm = getActivity().findViewById(R.id.my_state_confirm);
                        TextView myStateDecrease = getActivity().findViewById(R.id.my_state_deaths);
                        TextView myStateConfirm_inc = getActivity().findViewById(R.id.my_state_confirm_inc);
                        TextView myStateDeaths_inc = getActivity().findViewById(R.id.my_state_deaths_inc);
                        TextView myStateRecover_inc = getActivity().findViewById(R.id.my_state_recover_inc);

                        myState.setText(state);
                        myStateActive.setText(active);
                        myStateRecover.setText(recover);
                        myStateConfirm.setText(confirm);
                        myStateDecrease.setText(death);
                        myStateConfirm_inc.setText(confirm_inc);
                        myStateDeaths_inc.setText(death_inc);
                        myStateRecover_inc.setText(recover_inc);
                    }

                    // For 1st iterated the data is of All Over Country in API we have used.
                    if (i == 0) {
                        TextView updateTime = getActivity().findViewById(R.id.lastUpdate);
                        TextView countryActive = getActivity().findViewById(R.id.country_active);
                        TextView countryRecover = getActivity().findViewById(R.id.country_recover);
                        TextView countryConfirm = getActivity().findViewById(R.id.country_confirm);
                        TextView countryDeaths = getActivity().findViewById(R.id.country_deaths);
                        TextView countryConfirm_inc = getActivity().findViewById(R.id.country_confirm_inc);
                        TextView countryDeaths_inc = getActivity().findViewById(R.id.country_death_inc);
                        TextView countryRecover_inc = getActivity().findViewById(R.id.country_recover_inc);

                        updateTime.setText(timeFormatter(lastUpdated));
                        countryActive.setText(active);
                        countryRecover.setText(recover);
                        countryConfirm.setText(confirm);
                        countryDeaths.setText(death);
                        countryConfirm_inc.setText(confirm_inc);
                        countryDeaths_inc.setText(death_inc);
                        countryRecover_inc.setText(recover_inc);
                    }

                    // There is an bug in API, so to overcome that this is the mandatory condition.
                    if (!(singleStateData.getString("statecode").equals("UN") || i == 0)) {

                        state singleData = new state();

                        singleData.setState(state);
                        singleData.setActive(active);
                        singleData.setRecover(recover);
                        singleData.setConfirm(confirm);
                        singleData.setDeath(death);
                        singleData.setRecover_inc(recover_inc);
                        singleData.setDeath_inc(death_inc);
                        singleData.setConfirm_inc(confirm_inc);

                        allData.add(singleData);
                    }
                }
                // Adding data to the Adapter.
                adapter = new stateAdapter(allData);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            } catch (JSONException | ParseException error) {
                Log.d("Updates Fragment", "Something Related to JSON Parsing -> " + error);
            }
        }, error -> {
            Log.d("Updates Fragment", "Something Related to Volley -> " + error);
        });

        queue.add(jsonObjectRequest);
    }

    public void getLocation() {
        Dialog dialog = new Dialog(getContext());
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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    splashScreen.currentLocation = addressList.get(0).getAdminArea();
                } catch (IOException e) {
                    Log.d("IOException", "Error = " + e);
                }
            }
        }
    }
}
