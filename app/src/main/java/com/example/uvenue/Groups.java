package com.example.uvenue;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Groups {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("items")
    private List<VenueModel> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VenueModel> getItems() {
        return items;
    }

    public void setItems(List<VenueModel> items) {
        this.items = items;
    }

    // results list holding the "meat" of the venue data model
    List<VenueModel> results = new ArrayList<>();
}
