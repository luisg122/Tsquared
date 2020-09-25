package com.example.tsquared.Models;

public class NewsArticlesViewModel {
    public String source;
    public String titleOfNewsArticle;
    public String firstFewLines;
    public int imageOfNewsArticle;

    public NewsArticlesViewModel(String sourceOfArticle, String title, String firstLines, int image){
        source = sourceOfArticle;
        titleOfNewsArticle = title;
        firstFewLines = firstLines;
        imageOfNewsArticle = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitleOfNewsArticle() {
        return titleOfNewsArticle;
    }

    public void setTitleOfNewsArticle(String titleOfNewsArticle) {
        this.titleOfNewsArticle = titleOfNewsArticle;
    }

    public String getFirstFewLines() {
        return firstFewLines;
    }

    public void setFirstFewLines(String firstFewLines) {
        this.firstFewLines = firstFewLines;
    }

    public int getImageOfNewsArticle() {
        return imageOfNewsArticle;
    }

    public void setImageOfNewsArticle(int imageOfNewsArticle) {
        this.imageOfNewsArticle = imageOfNewsArticle;
    }
}
