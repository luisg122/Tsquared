package com.example.tsquared.Models;

public class MoreNewsModel {
    private int image;
    private String description;
    private String sourcePublisher;

    public MoreNewsModel() {
    }

    public MoreNewsModel(int image, String description) {
        this.image        = image;
        this.description  = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
