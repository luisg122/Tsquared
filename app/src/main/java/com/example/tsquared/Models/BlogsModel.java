package com.example.tsquared.Models;

public class BlogsModel {
    public String name;
    public String title;
    public String firstFewLines;
    public String image;

    public BlogsModel(String name, String title, String firstFewLines, String image){
        this.name = name;
        this.title = title;
        this.firstFewLines = firstFewLines;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstFewLines() {
        return firstFewLines;
    }

    public void setFirstFewLines(String firstFewLines) {
        this.firstFewLines = firstFewLines;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
