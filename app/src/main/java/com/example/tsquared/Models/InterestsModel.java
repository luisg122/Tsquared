package com.example.tsquared.Models;

import android.widget.CheckBox;

public class InterestsModel {
    public String subject = " ";
    public boolean selectedValue;

    public InterestsModel(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public void setSelected(boolean selectedValue){
        this.selectedValue = selectedValue;
    }

    public boolean isSelected(){
        return selectedValue;
    }
}
