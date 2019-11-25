package com.example.uvenue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    /**
     * This will show venues near philly
     * @return List of @ApiJSON results
     */
    @GET("/venues/explore?near=philly")
    Call<ApiJSON> getAllPhillyVenues();

    /**
     * This will show venues near your current specified location
     * @param near
     * @return List of @ApiJSON
     */
    @GET("/venues/explore?near={near}")
    Call<ApiJSON> getAllNearbyVenues(@Path("near") String near);

    /**
     * This will show venues near your current latitude and longitude
     * @param ll
     * @return List of @VenueModel
     */
    @GET("/venues/explore?ll={ll}")
    Call<ApiJSON> getLatLngNearbyVenues(@Path(value = "ll", encoded = true) String ll);


    @GET("/venues/search?intent=global")
    Call<List<ApiJSON>> search();
}
