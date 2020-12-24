package com.example.tsquared.Models;

public class DiscoverImageModel {
    public String imageURL;
    public String publisherName;
    public String description;

    public DiscoverImageModel(String imageURL, String publisherName, String description){
        this.imageURL = imageURL;
        this.publisherName = publisherName;
        this.description   = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
