package com.example.uvenue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class VenueQueryDialog extends AppCompatDialogFragment {
    private EditText editTextQuery;
    private EditText editTextLocation;
    private String query;
    private String near;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.venue_query_dialog, null);

        editTextLocation = view.findViewById(R.id.edit_location);
        editTextQuery = view.findViewById(R.id.edit_query);

        builder.setView(view).setTitle("Search Venues")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query = editTextQuery.getText().toString();
                        near = editTextLocation.getText().toString();
                        Intent searchIntent = new Intent(getContext(), QueryPlacesActivity.class);
//                        searchIntent.putExtra("name", "Current Location");
                        searchIntent.putExtra("ID", 000);
                        searchIntent.putExtra("latitude", 33.7891582);
                        searchIntent.putExtra("longitude", -84.38493509999999);
                        searchIntent.putExtra("query", query);
                        searchIntent.putExtra("location", near);
                        startActivity(searchIntent);
                    }
                });
        return builder.create();
    }
}
