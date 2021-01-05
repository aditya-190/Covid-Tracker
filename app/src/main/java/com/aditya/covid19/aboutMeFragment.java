package com.aditya.covid19;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

public class aboutMeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_me, container, false);

        TextView skills = view.findViewById(R.id.see_all_skills);
        TextView experiences = view.findViewById(R.id.see_all_experience);

        experiences.setOnClickListener(v -> {
            new seeAll().execute();
        });

        skills.setOnClickListener(v -> {
            new seeAll().execute();
        });

        return view;
    }

    public class seeAll extends AsyncTask<Void, Void, Void> {
        CustomTabsIntent customTabsIntent;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {

            customTabsIntent.launchUrl(getActivity(), Uri.parse("https://www.linkedin.com/in/adi-bhardwaj/"));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            progressDialog.setContentView(R.layout.loading_popup);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            customTabsIntent = builder.build();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler(Looper.myLooper()).postDelayed(() -> progressDialog.dismiss(), 600);
        }
    }
}