package com.example.tsquared.Models;

public class InterestsHorizontalModel {
    private String imageURL;
    private String description;
    private int interestID;

    public InterestsHorizontalModel() {
    }

    public InterestsHorizontalModel(int interestID, String image, String description){
        this.interestID  = interestID;
        this.imageURL    = image;
        this.description = description;
    }

    public int getInterestID() {
        return interestID;
    }

    public void setInterestID(int interestID) {
        this.interestID = interestID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImage(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
