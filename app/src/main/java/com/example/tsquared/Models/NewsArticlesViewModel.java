package com.example.tsquared.Models;

public class NewsArticlesViewModel {
    public String source;
    public String titleOfNewsArticle;
    public String firstFewLines;
    public String imageOfNewsArticle;
    public String URL;

    public NewsArticlesViewModel(String URL, String sourceOfArticle, String title, String firstLines, String image){
        this.URL = URL;
        source = sourceOfArticle;
        titleOfNewsArticle = title;
        firstFewLines = firstLines;
        imageOfNewsArticle = image;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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

    public String getImageOfNewsArticle() {
        return imageOfNewsArticle;
    }

    public void setImageOfNewsArticle(String imageOfNewsArticle) {
        this.imageOfNewsArticle = imageOfNewsArticle;
    }
}
