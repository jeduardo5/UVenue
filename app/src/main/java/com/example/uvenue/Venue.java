package com.example.uvenue;

import com.google.gson.annotations.SerializedName;

public class Venue {

    @SerializedName("id")
    String venueId;

    @SerializedName("name")
    String venueName;

    // venue rating
    double rating;

    // location object of venue
    VenueLocation location;

}
