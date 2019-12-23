package com.example.uvenue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryPlacesActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener{

    // The client object for connecting to the Google API
    private GoogleApiClient mGoogleApiClient;

    private GoogleMap mMap;

    //
    private final String TAG = "GeoPlacesActivity";

    // The RecyclerView and associated objects for displaying the nearby coffee spots
    private RecyclerView rv;
    private LinearLayoutManager rvManager;
    private RecyclerView.Adapter rvAdapter;

    // The base URL for the Foursquare API
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";

    // The client ID and client secret for
    // authenticating with the Foursquare API
    private String foursquareClientID;
    private String foursquareClientSecret;
    private final static String DATE = "20191101";

    private RetrofitInterface retrofitInterface;

    private String ll;
    Location mCurrentLocation = null;


    // The details of the venue that is being displayed.
    private String venueID;
    private String venueName;
    private double venueLatitude;
    private double venueLongitude;
    private String query;
    private String near;


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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Retrieves venues details from the intent sent from PlacePickerActivity
        Bundle venue = getIntent().getExtras();
        venueID = venue.getString("ID");
        venueName = venue.getString("name");
        venueLatitude = venue.getDouble("latitude");
        venueLongitude = venue.getDouble("longitude");
        query = venue.getString("query");
        near = venue.getString("location");
        setTitle(venueName);

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
//                Call<ApiJSON> venueCall = retrofitInterface.getLatLngNearbyVenues(ll, foursquareClientID, foursquareClientSecret, DATE);
                Call<ApiJSON> venueCall = retrofitInterface.getAllNearbyVenues(near, query, foursquareClientID, foursquareClientSecret, DATE );
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

//        // setup map fragment venue location view
        LatLng venue;
//        if (mCurrentLocation == null){
//            mCurrentLocation = ml);
//            venue = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        } else {
            venue = new LatLng(venueLatitude, venueLongitude);
//        }

        //add marker to map
        Marker marker  = mMap.addMarker(new MarkerOptions()
        .position(venue)
        .title(venueName)
        .snippet("Click to check this venue out!!!"));

        marker.hideInfoWindow();
        //animate camera to view marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(venue).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Shows the user's current location
            mMap.setMyLocationEnabled(true);
        }

        //show marker info
        marker.showInfoWindow();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        // Opens the Foursquare venue page when a user clicks on the info window of the venue
        Intent venueIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://foursquare.com/v/" + venueID));
        startActivity(venueIntent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //show marker info
        marker.showInfoWindow();

        return true;
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

