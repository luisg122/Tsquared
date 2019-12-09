package com.example.tsquared;

public class QuestionViewModel {

    private String answer;

    public QuestionViewModel(String answer){
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
