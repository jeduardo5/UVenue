package com.example.uvenue;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoPlacesActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // The client object for connecting to the Google API
    private GoogleApiClient mGoogleApiClient;

    //
    private final String TAG = "GeoPlacesActivity";

    // The RecyclerView and associated objects for displaying the nearby coffee spots
    private RecyclerView rv;
    private LinearLayoutManager rvManager;
    private RecyclerView.Adapter rvAdapter;

    // The base URL for the Foursquare API
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";

    // The client ID and client secret for authenticating with the Foursquare API
    private String foursquareClientID;
    private String foursquareClientSecret;
    private final static String DATE = "20191101";

    private RetrofitInterface retrofitInterface;

    private String ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        //getActionBar().setHomeButtonEnabled(true);
       // getActionBar().setDisplayHomeAsUpEnabled(true);

        // The visible TextView and RecyclerView objects
        rv = (RecyclerView)findViewById(R.id.venueList);

        // Sets the dimensions, LayoutManager, and dividers for the RecyclerView
        rv = findViewById(R.id.venueList);
        rvManager = new LinearLayoutManager(this);
        rv.setLayoutManager(rvManager);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), rvManager.getOrientation()));

        // Creates a connection to the Google API for location services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Gets the stored Foursquare API client ID and client secret from XML
        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        // Checks for location permissions at runtime (required for API >= 23)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Makes a Google API request for the user's last known location
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                ll = mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude();
                retrofitInterface = RetrofitInstance.getRetrofitInstance().create(RetrofitInterface.class);
                Call<ApiJSON> venueCall = retrofitInterface.getLatLngNearbyVenues(ll, foursquareClientID, foursquareClientSecret, DATE);
//                Call<ApiJSON> venueCall = retrofitInterface.getAllPhillyVenues(foursquareClientID, foursquareClientSecret, DATE );
                venueCall.enqueue(new Callback<ApiJSON>() {
                    @Override
                    public void onResponse(Call<ApiJSON> call, Response<ApiJSON> response) {
                        Log.e("ApiResponse", ""+ response.code());
                        if (response.isSuccessful()){
                            Gson gson = new Gson();
                            String responseMsg = gson.toJson(response.body());
                            Log.d(TAG, responseMsg);
                            Log.d(TAG, response.toString());
                            Log.d(TAG, response.body().toString());
                        }
                        //Get venue data object from JSON response
                        ApiJSON rjson = response.body();
                        ApiResponse rf = rjson.response;
                        Groups rfs = (Groups) rf.getGroups().get(0);
                        List<VenueModel> venueData = rfs.getItems();

                        //Display results in our recyclerview
                        rvAdapter = new RecyclerViewAdapter(getApplicationContext(), venueData);
                        rv.setAdapter(rvAdapter);

                    }

                    @Override
                    public void onFailure(Call<ApiJSON> call, Throwable t) {
                        Log.e("ApiJSON", t.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), "We can't connect to servers and thus cannot use your location", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "We can't find your current location", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Do Something
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Do Something
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "We can't connect to Google's servers!", Toast.LENGTH_LONG).show();
        finish();
    }

}

