package com.aditya.covid19.mediaSegment;

import java.util.List;

public class categoryTitle {
    private String categoryTitle;
    private List<news> newsList;

    public categoryTitle() {
    }

    public categoryTitle(String categoryTitle, List<news> newsList) {
        this.categoryTitle = categoryTitle;
        this.newsList = newsList;
    }

    public categoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<news> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<news> newsList) {
        this.newsList = newsList;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}
