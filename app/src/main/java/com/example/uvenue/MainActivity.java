package com.example.uvenue;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ACCESS_LOCATION_PERMISSION = 1;
    private ImageView currentLocationImg;
    private ImageView queryLocationImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(this, LocationService.class));

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, ACCESS_LOCATION_PERMISSION);
        }

        // Attaches a listener for onclick actions
        currentLocationImg = findViewById(R.id.logo1);
        queryLocationImg = findViewById(R.id.logo2);
        currentLocationImg.setOnClickListener(this);
        queryLocationImg.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo1:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(getApplicationContext(), GeoPlacesActivity.class);
                    i.putExtra("name", "Current Location");
                    i.putExtra("ID", 000);
                    i.putExtra("latitude", 33.7891582);
                    i.putExtra("longitude", -84.38493509999999);
                    startActivity(i);
                } else {
                    // Notifies the user if there are insufficient location permissions
                    Toast.makeText(getApplicationContext(), "Missing permissions to access your location!", Toast.LENGTH_LONG).show();
                }
            case R.id.logo2:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                  openDialog();
                } else {
                    // Notifies the user if there are insufficient location permissions
                    Toast.makeText(getApplicationContext(), "Missing permissions to access your location!", Toast.LENGTH_LONG).show();
                }
        }
    }

    public void openDialog() {
        VenueQueryDialog queryDialog = new VenueQueryDialog();
        queryDialog.show(getSupportFragmentManager(), "Query Dialog");
    }
}
