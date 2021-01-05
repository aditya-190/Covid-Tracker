package com.aditya.covid19.mediaSegment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.covid19.R;

import java.util.List;

public class categoryTitleAdapter extends RecyclerView.Adapter<categoryTitleAdapter.categoryTitleViewHolder> {
    private final List<categoryTitle> titles;

    public categoryTitleAdapter(List<categoryTitle> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public categoryTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new categoryTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_title_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull categoryTitleViewHolder holder, int position) {
        holder.categoryTitle.setText(titles.get(position).getCategoryTitle());

        newsAdapter newsAdapter = new newsAdapter(titles.get(position).getNewsList());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), RecyclerView.HORIZONTAL, false));
        holder.recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class categoryTitleViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView recyclerView;

        public categoryTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
            recyclerView = itemView.findViewById(R.id.news_recycler);
        }
    }
}
