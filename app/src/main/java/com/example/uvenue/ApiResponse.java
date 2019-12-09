package com.example.uvenue;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {


    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getSuggestedRadius() {
        return suggestedRadius;
    }

    public void setSuggestedRadius(int suggestedRadius) {
        this.suggestedRadius = suggestedRadius;
    }

    public String getHeaderFullLocation() {
        return headerFullLocation;
    }

    public void setHeaderFullLocation(String headerFullLocation) {
        this.headerFullLocation = headerFullLocation;
    }

    public String getHeaderLocationGranularity() {
        return headerLocationGranularity;
    }

    public void setHeaderLocationGranularity(String headerLocationGranularity) {
        this.headerLocationGranularity = headerLocationGranularity;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }


    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("suggestedRadius")
    private int suggestedRadius;

    @SerializedName("headerFullLocation")
    private String headerFullLocation;

    @SerializedName("headerLocationGranularity")
    private String headerLocationGranularity;

    @SerializedName("groups")
    private List<Groups> groups;


    @SerializedName("headerLocation")
    private String headerLocation;


    List<Venue> venues = new ArrayList<>();

}
