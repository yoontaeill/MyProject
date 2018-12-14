package com.example.edati.movie;


import java.util.Date;

public class Movie {
    String image;
    String title;
    String director;
    String actor;
    String pubDate;
    float userRating;
    String link;

    public Movie(String image, String title,String pubDate, String director, String actor, int userRating, String link) {
        this.image = image;
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = userRating;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
