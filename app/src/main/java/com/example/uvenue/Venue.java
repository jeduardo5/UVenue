package com.example.uvenue;

import com.google.gson.annotations.SerializedName;

public class Venue {


    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public VenueLocation getLocation() {
        return location;
    }

    public void setLocation(VenueLocation location) {
        this.location = location;
    }

    @SerializedName("id")
    private String id;

//    @SerializedName("categories")
//    private List<CategoriesItem> categories;
//
//    @SerializedName("photos")
//    private Photos photos;
//
//    @SerializedName("delivery")
//    private Delivery delivery;
//
//    @SerializedName("venuePage")
//    private VenuePage venuePage;

    // venue rating
    @SerializedName("rating")
    private double rating;

    // location object of venue
    @SerializedName("location")
    private VenueLocation location;

}
