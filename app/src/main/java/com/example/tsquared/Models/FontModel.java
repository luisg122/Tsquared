package com.example.tsquared.Models;

public class FontModel {
    private String font;
    private String fontName;

    public FontModel(String font, String fontName){
        this.font = font;
        this.fontName = fontName;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }
}
