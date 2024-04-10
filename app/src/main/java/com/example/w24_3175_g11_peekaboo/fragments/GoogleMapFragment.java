package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.w24_3175_g11_peekaboo.R;

public class GoogleMapFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_googlemap, container, false);

        EditText editTextSource = view.findViewById(R.id.editTextSourceLocation);
        EditText editTextDestination = view.findViewById(R.id.editTextDestinationLocation);
        Button btnOpenNavigation = view.findViewById(R.id.BtnOpenNavigation);
        editTextDestination.setText("Kids Canvas Childcare");

        //set up the onclick listener for the button
        btnOpenNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the intent to start the google map app
                String sourceLocation = editTextSource.getText().toString();
                String destinationLocation = editTextDestination.getText().toString();
                if (sourceLocation.isEmpty() && destinationLocation.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter both source and destination", Toast.LENGTH_SHORT).show();

                } else {
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/" + sourceLocation + "/" + destinationLocation);
                    Intent intentNavigation = new Intent(Intent.ACTION_VIEW, uri);
                    intentNavigation.setPackage("com.google.android.apps.maps");//package name of google maps
                    intentNavigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentNavigation);
                }

            }
        });

        return view;

    }
}
