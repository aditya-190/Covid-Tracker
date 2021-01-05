package com.aditya.covid19.mediaSegment;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.aditya.covid19.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.newsViewHolder> {
    private final List<news> news;

    public newsAdapter(List<news> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new newsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_news_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        holder.newsDescription.setText(news.get(position).getNewsDescription());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(holder.itemView.getContext()).load(news.get(position).getNewsThumbnail()).placeholder(circularProgressDrawable).transition(DrawableTransitionOptions.withCrossFade()).into(holder.newsThumbnail);

        holder.itemView.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(holder.itemView.getContext(), Uri.parse(news.get(position).getNewsUrl()));
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class newsViewHolder extends RecyclerView.ViewHolder {

        TextView newsDescription;
        ImageView newsThumbnail;

        public newsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsDescription = itemView.findViewById(R.id.video_description);
            newsThumbnail = itemView.findViewById(R.id.video_thumbnail);
        }
    }
}
