package com.example.uvenue;


import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    // The application context for getting resources
    private Context context;

    //current location
    private Location mLastLocation;

    // The list of results from the Foursquare API
    private List<VenueModel> results;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The venue fields to display
        TextView name;
        TextView address;
        TextView rating;
        TextView distance;
        String id;
        String near;
        String query;
        double latitude;
        double longitude;


        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            // Gets the appropriate view for each venue detail
            name = (TextView)v.findViewById(R.id.placePickerItemName);
            address = (TextView)v.findViewById(R.id.placePickerItemAddress);
            rating = (TextView)v.findViewById(R.id.placePickerItemRating);
            distance = (TextView)v.findViewById(R.id.placePickerItemDistance);
        }

        @Override
        public void onClick(View v) {

            // Creates an intent to direct the user to a map view
            Context context = name.getContext();
            Intent i = new Intent(context, GeoPlacesActivity.class);

            // Passes the crucial venue details onto the map view
            i.putExtra("name", name.getText());
            i.putExtra("ID", id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);

            // Transitions to the map view.
            context.startActivity(i);
        }
    }

    public RecyclerViewAdapter(Context context, List<VenueModel> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_places, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        // Sets the proper rating colour, referenced from the Foursquare Brand Guide
        double ratingRaw = results.get(position).getVenue().getRating();
//        if (ratingRaw >= 9.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQKale));
//        } else if (ratingRaw >= 8.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQGuacamole));
//        } else if (ratingRaw >= 7.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQLime));
//        } else if (ratingRaw >= 6.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQBanana));
//        } else if (ratingRaw >= 5.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQOrange));
//        } else if (ratingRaw >= 4.0) {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQMacCheese));
//        } else {
//            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQStrawberry));
//        }

        // Sets each view with the appropriate venue details
        holder.name.setText(results.get(position).getVenue().getName());
        holder.address.setText(results.get(position).getVenue().getLocation().getAddress());
        holder.rating.setText(Double.toString(ratingRaw));
        holder.distance.setText(Integer.toString(results.get(position).getVenue().getLocation().getDistance()) + "m");

        // Stores additional venue details for the map view
        holder.id = results.get(position).getVenue().getId();
        holder.latitude = results.get(position).getVenue().getLocation().getLat();
        holder.longitude = results.get(position).getVenue().getLocation().getLng();
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}