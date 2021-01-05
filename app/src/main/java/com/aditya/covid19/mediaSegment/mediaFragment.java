package com.aditya.covid19.mediaSegment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aditya.covid19.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mediaFragment extends Fragment {
    public static RecyclerView categoryTitleRecycler;
    public static categoryTitleAdapter categoryTitleAdapter;
    public static SwipeRefreshLayout refreshMedia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_fragment, container, false);

        categoryTitleRecycler = view.findViewById(R.id.category_title_recycler);
        refreshMedia = view.findViewById(R.id.media_refresh);

        fetchMediaData();

        refreshMedia.setOnRefreshListener(this::fetchMediaData);

        return view;
    }

    public void fetchMediaData() {
        refreshMedia.setRefreshing(true);
        checkConnection();
        ArrayList<categoryTitle> allData = new ArrayList<>();

        List<String> categoryTitles = new ArrayList<>();
        categoryTitles.add(0, "Trending Now");
        categoryTitles.add(1, "How To Boost Immunity");
        categoryTitles.add(2, "Together We Can");
        categoryTitles.add(3, "Advice From Doctors");
        categoryTitles.add(4, "Celebrity Videos");
        categoryTitles.add(5, "Learning Centre");

        List<String> queryForURL = new ArrayList<>();
        queryForURL.add(0, "covid%2019%20Trending");
        queryForURL.add(1, "covid%20immunity");
        queryForURL.add(2, "covid%20spirit");
        queryForURL.add(3, "doctor%20advice");
        queryForURL.add(4, "bollywood");
        queryForURL.add(5, "learn%20about%20covid");

        RequestQueue queue = Volley.newRequestQueue(getContext());

        for (int i = 0; i < categoryTitles.size(); i++) {
            String JSON_URL = "https://newsapi.org/v2/everything?q=" + queryForURL.get(i) + "&apikey=a13cd2a274a84257ad4b8ce468e0180a";

            int finalI = i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    JSON_URL, null, response -> {
                try {
                    // Retrieve an JSONObject Array on Videos Details.
                    JSONArray totalNews = response.getJSONArray("articles");

                    ArrayList<news> newsMajors = new ArrayList<>();

                    // Iterating
                    for (int j = 0; j < totalNews.length(); j++) {

                        // All Data for an single News.
                        JSONObject singleNews = totalNews.getJSONObject(j);

                        news news = new news();
                        news.setNewsDescription(singleNews.getString("title"));
                        news.setNewsThumbnail(singleNews.getString("urlToImage"));
                        news.setNewsUrl(singleNews.getString("url"));

                        newsMajors.add(news);
                    }

                    categoryTitle singleNewsData = new categoryTitle();
                    singleNewsData.setCategoryTitle(categoryTitles.get(finalI));
                    singleNewsData.setNewsList(newsMajors);

                    allData.add(singleNewsData);

                    // Adding data to the Adapter.
                    mediaFragment.categoryTitleAdapter = new categoryTitleAdapter(allData);

                    mediaFragment.categoryTitleRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    mediaFragment.categoryTitleRecycler.setAdapter(mediaFragment.categoryTitleAdapter);

                    mediaFragment.categoryTitleAdapter.notifyDataSetChanged();
                    refreshMedia.setRefreshing(false);

                } catch (JSONException error) {
                    Log.d("Media Fragment", "Something Related to JSON Parsing -> " + error);
                }
            }, error -> {
                Log.d("Media Fragment", "Something Related to Volley -> " + error);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0");
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        }
    }

    public void checkConnection() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.no_internet_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView checkInternet = dialog.findViewById(R.id.check_internet);
        TextView goToSettings = dialog.findViewById(R.id.goto_settings);

        goToSettings.setOnClickListener(v -> {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }
}
