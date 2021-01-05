package com.aditya.covid19;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aditya.covid19.mediaSegment.mediaFragment;
import com.aditya.covid19.updatesSegment.updatesFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Check Internet Connection.
        checkConnection();

        // Toolbar Setup Process
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Set Up TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(viewPagerAdapter);

        // Add Text and Icon to the TabLayout
        tabLayout.getTabAt(0).setIcon(R.drawable.tab_icon_updates).setText("COVID Updates");
        tabLayout.getTabAt(1).setIcon(R.drawable.tab_icon_media).setText("Media");
        tabLayout.getTabAt(2).setIcon(R.drawable.tab_icon_about_me).setText("About Me");
    }

    public void checkConnection() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_internet_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView checkInternet = dialog.findViewById(R.id.check_internet);
        TextView goToSettings = dialog.findViewById(R.id.goto_settings);

        goToSettings.setOnClickListener(v -> {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
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

    // Used to Bind the items defined in menu.xml in the Menu Folder to the Action Bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Called When the Icon Share on The Action Bar is Clicked.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check out this New App Made By Me in recent.\nYour Opinions and feedback are most welcomed.");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Aditya Bhardwaj");
            startActivity(Intent.createChooser(sharingIntent, "Share App Link Via :"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class viewPagerAdapter extends FragmentPagerAdapter {
        public viewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new mediaFragment();
                case 2:
                    return new aboutMeFragment();
                default:
                    return new updatesFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

