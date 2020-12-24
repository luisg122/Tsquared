package com.example.tsquared.Models;

public class NewsHorizontalModel {
    private String imageURL;
    private String description;
    private String URL;

    public NewsHorizontalModel() {
    }

    public NewsHorizontalModel(String URL, String imageURL, String description) {
        this.URL          = URL;
        this.imageURL     = imageURL;
        this.description  = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}