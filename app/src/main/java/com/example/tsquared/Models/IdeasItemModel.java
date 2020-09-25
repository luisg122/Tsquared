package com.example.tsquared.Models;

public class IdeasItemModel {
    public String title = "";
    public String desc  = "";
    public String id    = "";

    public IdeasItemModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdeasItemModel(String id, String title, String desc){
        this.title = title;
        this.desc  = desc;
        this.id    = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}