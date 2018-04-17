package com.depex.okeyclick.user.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The class is used for Inner view pager recycler
 */

public class BookLaterServiceProvider {



    @SerializedName("first_name")
    private String name;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("id")
    private String id;
    @SerializedName("distance")
    private String distance;
    @SerializedName("rating")
    private String rating;
    @SerializedName("hr_price")
    private String pricePerHour;
    @SerializedName("sp_profile")
    private String imageUrl;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("category_list")
    List<Service> services;
    @SerializedName("bio")
    private String bio;
    @SerializedName("exp")
    private String exp;
    @SerializedName("about_company")
    private String aboutCompany;

    @SerializedName("reviews")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

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

    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
    public static BookLaterServiceProvider fromJson(String json){
        //2018-04-10 11:29:50
        Gson gson=new Gson();
        return gson.fromJson(json, BookLaterServiceProvider.class);
    }
}