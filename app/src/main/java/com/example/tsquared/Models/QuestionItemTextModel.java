package com.example.tsquared.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionItemTextModel {

    public  int      isAnonymous;
    public  String   name;
    public  String   topic;
    public  String   question;
    public  String   dateSubmitted;
    public  String   responseNum;
    public  int profileImage;

    QuestionItemTextModel() {

    }
    public QuestionItemTextModel(String topic, String question,
                                 String dateSubmitted, String responseNum){
        this.topic    = topic;
        this.question = question;
        this.dateSubmitted = dateSubmitted;
        this.responseNum   = responseNum;

        if(this.responseNum.equals("1"))
            this.responseNum = this.responseNum + " Answer";
        else
            this.responseNum = this.responseNum + " Answers";

    }

    public static QuestionItemTextModel fromJson(JSONObject jsonObject) throws JSONException {
        QuestionItemTextModel question = new QuestionItemTextModel();
        question.isAnonymous   = jsonObject.getInt("isAnonymous");
        question.name          = jsonObject.getString("PostedBy");
        question.topic         = jsonObject.getString("QuestionDetails");
        question.question      = jsonObject.getString("Content");
        question.dateSubmitted = jsonObject.getString("DatePosted");
        question.responseNum   = jsonObject.getString("ResponseNumber");

        if(question.responseNum.equals("1"))       question.responseNum = question.responseNum + " Answer";
        else if(!question.responseNum.equals("1")) question.responseNum = question.responseNum + " Answers";
        if(question.isAnonymous == 1){
            question.name = "Anonymous";
        }

        // question.name  = capitalizeFirstCharOfEveryWordInString(question.name);
        // question.topic = capitalizeFirstCharOfEveryWordInString(question.topic);

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

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
