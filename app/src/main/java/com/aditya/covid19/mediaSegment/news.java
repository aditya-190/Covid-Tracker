package com.aditya.covid19.mediaSegment;

public class news {
    private String newsDescription;
    private String newsThumbnail;
    private String newsUrl;

    public news(String newsDescription, String newsThumbnail, String newsUrl) {
        this.newsDescription = newsDescription;
        this.newsThumbnail = newsThumbnail;
        this.newsUrl = newsUrl;
    }

    public news() {

    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsThumbnail() {
        return newsThumbnail;
    }

    public void setNewsThumbnail(String newsThumbnail) {
        this.newsThumbnail = newsThumbnail;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
