package com.depex.okeyclick.user.model;

/**
 * The class is used for Inner view pager recycler
 */

public class BookLaterServiceProvider {


    private String name;
    private String id;
    private String distance;
    private float rating;
    private String pricePerHour;
    private String imageUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(String pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}