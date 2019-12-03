package com.example.finalproject;

import java.io.Serializable;

public class Article implements Serializable {

    private String urlToImage;
    private String title;
    private String description;

    public Article() {
        this.setUrlToImage(urlToImage);
        this.setTitle(title);
        this.setDescription(description);
    }


    public void setUrlToImage(String UrlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

