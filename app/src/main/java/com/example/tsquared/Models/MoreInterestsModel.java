package com.example.tsquared.Models;

public class MoreInterestsModel {
    private String interestsImageURL;
    private String interestsName;
    private int interestID;

    public MoreInterestsModel(){

    }

    public MoreInterestsModel(int interestID, String interestsImageURL, String interestsName){
        this.interestID = interestID;
        this.interestsImageURL = interestsImageURL;
        this.interestsName  = interestsName;
    }

    public int getInterestID() {
        return interestID;
    }

    public void setInterestID(int interestID) {
        this.interestID = interestID;
    }

    public String getInterestsImageURL() {
        return interestsImageURL;
    }

    public void setInterestsImageURL(String interestsImage) {
        this.interestsImageURL = interestsImage;
    }

    public String getInterestsName() {
        return interestsName;
    }

    public void setInterestsName(String interestsName) {
        this.interestsName = interestsName;
    }
}
