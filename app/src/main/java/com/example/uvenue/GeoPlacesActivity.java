package com.example.uvenue;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.uvenue.RetrofitInterface;
import com.example.uvenue.RetrofitInstance;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoPlacesActivity extends Activity {
    private RecyclerView rv;
    private LinearLayoutManager rvManager;
    private RecyclerView.Adapter rvAdapter;
    private RetrofitInterface retrofitInterface;
    private Location lastLocation;
    private String ll;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        //start service to determine current location

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("LocationUpdates"));

        rv = findViewById(R.id.venueList);
        rvManager = new LinearLayoutManager(this);
        rv.setLayoutManager(rvManager);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), rvManager.getOrientation()));


        //Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (lastLocation != null) {

                retrofitInterface = RetrofitInstance.getRetrofitInstance().create(RetrofitInterface.class);
                Call<ApiJSON> venueCall = retrofitInterface.getLatLngNearbyVenues(ll);
                venueCall.enqueue(new Callback<ApiJSON>() {
                    @Override
                    public void onResponse(Call<ApiJSON> call, Response<ApiJSON> response) {
                        //Get venue data object from JSON response
                        ApiJSON rjson = response.body();
                        ApiResponse rf = rjson.response;
                        Groups rfs = rf.groups;
                        List<VenueModel> venueData = rfs.results;

                        //Display results in our recyclerview
                        rvAdapter = new RecyclerViewAdapter(getApplicationContext(), venueData);
                        rv.setAdapter(rvAdapter);

                    }

                    @Override
                    public void onFailure(Call<ApiJSON> call, Throwable t) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Do Something
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ll = intent.getStringExtra("LatLng");
            Bundle b = intent.getBundleExtra("Location");
            lastLocation = (Location) b.getParcelable("Location");
        }
    };
}

