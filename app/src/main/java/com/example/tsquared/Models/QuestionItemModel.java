package com.example.tsquared.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionItemModel {
    public  String   name;
    public  String   topic;
    public  String   postTitle;
    public  String   postPreviewInformation;
    public  String   dateSubmitted;
    public  String   responseNum;
    public  String   profileImage;
    public  String   profileName;
    public  String   imageContent;
    public  String   urlImage;
    public  String   headline;
    public  String   source;
    public  boolean  isAnonymous;

    QuestionItemModel() {}

    public QuestionItemModel(String topic, String postTitle, String postPreviewInformation, String dateSubmitted, String responseNum,
                             String profileImage, String profileName, String imageContent,
                             String urlImage, String headline, String source) {

        this.topic    = topic;
        this.postTitle = postTitle;
        this.postPreviewInformation = postPreviewInformation;
        this.dateSubmitted = dateSubmitted;
        this.responseNum   = responseNum;
        this.profileImage  = profileImage;
        this.profileName   = profileName;
        this.imageContent  = imageContent;
        this.urlImage = urlImage;
        this.headline = headline;
        this.source = source;

        if(this.responseNum.equals("1"))
            this.responseNum = this.responseNum + " Answer";
        else
            this.responseNum = this.responseNum + " Answers";

    }

    public static QuestionItemModel fromJson(final JSONObject jsonObject) throws JSONException {
        QuestionItemModel question = new QuestionItemModel();
        question.isAnonymous   = jsonObject.getBoolean("isAnonymous");
        question.name          = jsonObject.getString("PostedBy");
        question.topic         = jsonObject.getString("QuestionDetails");
        question.postTitle = jsonObject.getString("Content");
        question.dateSubmitted = jsonObject.getString("DatePosted");
        question.responseNum   = jsonObject.getString("ResponseNumber");

        if(question.responseNum.equals("1"))       question.responseNum = question.responseNum + " Answer";
        else question.responseNum = question.responseNum + " Answers";

        if(question.isAnonymous) {
            question.name = "Anonymous";
        }

        return question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public String getResponseNum() {
        return responseNum;
    }

    public void setResponseNum(String responseNum) {
        this.responseNum = responseNum;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
