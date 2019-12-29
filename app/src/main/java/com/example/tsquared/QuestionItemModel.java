package com.example.tsquared;

import android.graphics.drawable.Drawable;

import com.google.android.gms.games.quest.Quest;

public class QuestionItemModel {
    private String name;
    private String topic;
    private String question;
    private String dateSubmitted;
    private String responseNum;
    private Drawable profileImage;

    public QuestionItemModel() {

    }

    public QuestionItemModel(String name, String topic, String question,
                             String dateSubmitted, String responseNum, Drawable profileImage){
        this.name     = name;
        this.topic    = topic;
        this.question = question;
        this.dateSubmitted = dateSubmitted;
        this.responseNum   = responseNum;
        this.profileImage  = profileImage;

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public Drawable getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Drawable profileImage) {
        this.profileImage = profileImage;
    }
}
