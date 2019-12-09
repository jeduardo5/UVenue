package com.example.uvenue;

import com.google.gson.annotations.SerializedName;

public class VenueModel {

    //A venue object from results
    @SerializedName("venue")
    private Venue venue;

    @SerializedName("referralId")
    private String referralId;

    @SerializedName("summary")
    private String summary;

    @SerializedName("reasonName")
    private String reasonName;

    @SerializedName("type")
    private String type;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }


    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
//VenueDetails details;
}
