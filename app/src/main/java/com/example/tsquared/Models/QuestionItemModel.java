package com.example.tsquared.Models;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionItemModel {

    public  int      isAnonymous;
    public  String   name;
    public  String   topic;
    public  String   question;
    public  String   dateSubmitted;
    public  String   responseNum;
    public  int profileImage;

    QuestionItemModel() {

    }
    public QuestionItemModel(String name, String topic, String question,
                             String dateSubmitted, String responseNum, int profileImage){
        this.name     = name;
        this.topic    = topic;
        this.question = question;
        this.dateSubmitted = dateSubmitted;
        this.responseNum   = responseNum;
        this.profileImage  = profileImage;

    }

    public static QuestionItemModel fromJson(JSONObject jsonObject) throws JSONException {
        QuestionItemModel question = new QuestionItemModel();
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
        question.name  = capitalizeFirstCharOfEveryWordInString(question.name);
        question.topic = capitalizeFirstCharOfEveryWordInString(question.topic);

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

    private static String capitalizeFirstCharOfEveryWordInString(String string){
        char[] ch = string.toCharArray();
        for(int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z'){
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }
}
