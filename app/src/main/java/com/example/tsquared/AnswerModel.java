package com.example.tsquared;

import android.graphics.drawable.Drawable;

public class AnswerModel {
    String name;
    String dateAnswered;
    String answer;
    Drawable profileImage;

    AnswerModel(String name, String dateAnswered, String answer, Drawable profileImage){
        this.name = name;
        this.dateAnswered = dateAnswered;
        this.answer = answer;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAnswered() {
        return dateAnswered;
    }

    public void setDateAnswered(String dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Drawable getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Drawable profileImage) {
        this.profileImage = profileImage;
    }
}
