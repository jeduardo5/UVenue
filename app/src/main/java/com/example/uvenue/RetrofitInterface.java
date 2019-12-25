package com.example.uvenue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    /**
     * This will show venues near philly
     * @return List of @ApiJSON results
     */
    @GET("v2/venues/explore?near=philly")
    Call<ApiJSON> getAllPhillyVenues(  @Query("client_id") String client_id,
                                       @Query("client_secret") String client_secret,
                                       @Query("v") String date);

    /**
     * This will show venues near your current specified location
     * @param near
     * @return List of @ApiJSON
     */
    @GET("v2/venues/explore?v=20191101")
    Call<ApiJSON> getAllNearbyVenues(@Query("near") String near,
                                     @Query("ll") String ll,
                                     @Query("query") String query,
                                     @Query("client_id") String client_id,
                                     @Query("client_secret") String client_secret,
                                     @Query("v") String date
                                     );

    /**
     * This will show venues near your current latitude and longitude
     * @param ll
     * @return List of @VenueModel
     */
    @GET("v2/venues/explore?")
    Call<ApiJSON> getLatLngNearbyVenues(@Query("ll") String ll,
                                        @Query("client_id") String client_id,
                                        @Query("client_secret") String client_secret,
                                        @Query("v") String date
                                        );


    @GET("v2/venues/search?intent=global")
    Call<List<ApiJSON>> search();
}
