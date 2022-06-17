package com.example.tsquared.Models;

public class UserArticleModel {
    private String imageURL;
    private String description;
    private String sourcePublisher;
    private String URL;

    public UserArticleModel() {
    }

    public UserArticleModel(String URL, String imageURL, String description, String sourcePublisher) {
        this.URL = URL;
        this.imageURL     = imageURL;
        this.description  = description;
        this.sourcePublisher = sourcePublisher;
    }

    public String getSourcePublisher() {
        return sourcePublisher;
    }

    public void setSourcePublisher(String sourcePublisher) {
        this.sourcePublisher = sourcePublisher;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
}
